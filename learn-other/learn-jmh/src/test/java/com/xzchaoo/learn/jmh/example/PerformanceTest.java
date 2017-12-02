package com.xzchaoo.learn.jmh.example;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.WorkerPool;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * created by xzchaoo at 2017/12/2
 *
 * @author xzchaoo
 */
class Helper {
	public static double process(FooEvent e) {
		double sum = 0;
		for (int i = 0; i < 100; ++i) {
			switch (i % 3) {
				case 0:
					sum += Math.sin(e.getValue());
					break;
				case 1:
					sum += Math.cos(e.getValue());
					break;
				case 2:
					sum += Math.sqrt(e.getValue());
					break;
			}
		}
		return sum;
	}
}

@BenchmarkMode({Mode.Throughput, Mode.SingleShotTime})
@Warmup(iterations = 2)
@Measurement(iterations = 3)
@Fork(1)
public class PerformanceTest {
	public static final int n = 1000000;

	@Benchmark
	@OperationsPerInvocation(n)
	public void test1() throws InterruptedException {
		AtomicLong sum = new AtomicLong(0);
		AtomicLong counter = new AtomicLong(0);
		for (int i = 0; i < n; ++i) {
			FooEvent e = new FooEvent(i);
			Helper.process(e);
			sum.addAndGet(e.getValue());
			counter.incrementAndGet();
		}
	}

	@State(Scope.Benchmark)
	public static class ABQState {
		ArrayBlockingQueue<FooEvent> abq;
		ExecutorService es;
		AtomicLong counter;
		int threads = 12;

		@Setup
		public void setup() {
			abq = new ArrayBlockingQueue<FooEvent>(16384);
			es = Executors.newFixedThreadPool(threads);
			counter = new AtomicLong();
			ABQConsumer consumer = new ABQConsumer(abq, counter);
			for (int i = 0; i < threads; ++i) {
				es.execute(consumer);
			}
		}

		@TearDown
		public void td() {
			es.shutdownNow();
		}
	}

	@Benchmark
	@OperationsPerInvocation(n)
	public void test_abq(ABQState state) throws InterruptedException {
		ArrayBlockingQueue<FooEvent> abq = state.abq;
		AtomicLong counter = state.counter;
		counter.set(0);
		for (int i = 0; i < n; ++i) {
			FooEvent e = new FooEvent(i);
			abq.put(e);
		}
		while (counter.get() != n) {
			Thread.yield();
		}
	}

	@State(Scope.Benchmark)
	public static class DState {
		int threads = 12;
		ExecutorService es;
		int bufferSize = 131072;
		FooEventFactory lef = new FooEventFactory();
		private Disruptor<FooEvent> d;
		private AtomicLong counter;
		private RingBuffer<FooEvent> rb;
		private WorkerPool<FooEvent> workerPool;

		@Setup
		public void setup() {
			es = Executors.newFixedThreadPool(threads);
			//YieldingWaitStrategy BusySpinWaitStrategy
			d = new Disruptor<>(lef, bufferSize, es, ProducerType.SINGLE, new BusySpinWaitStrategy());
			counter = new AtomicLong();
//			d.handleEventsWith(
//				new FooEventHandler(counter)
//			);

			FooWorkHandler[] workers=new FooWorkHandler[threads];
			for(int i=0;i<threads;++i){
				workers[i]=new FooWorkHandler(counter);
			}
			d.handleEventsWithWorkerPool(
				workers
			);

			rb = d.start();

//			SequenceBarrier sb = rb.newBarrier();
//			workerPool = new WorkerPool<FooEvent>(rb, sb, new IgnoreExceptionHandler(),
//				new FooWorkHandler(counter),
//				new FooWorkHandler(counter),
//				new FooWorkHandler(counter),
//				new FooWorkHandler(counter)
//			);
//			workerPool.start(es);
		}

		@TearDown
		public void td() {
			//workerPool.drainAndHalt();
			d.shutdown();
			es.shutdownNow();
		}
	}

	@Benchmark
	@OperationsPerInvocation(n)
	public void test_disruptor(DState state) throws Exception {
		AtomicLong counter = state.counter;
		counter.set(0);
		RingBuffer<FooEvent> rb = state.rb;
		for (int i = 0; i < n; ++i) {
			long sq = rb.next();
			try {
				FooEvent e = rb.get(sq);
				e.setValue(i);
			} finally {
				rb.publish(sq);
			}
		}
		while (counter.get() != n) {
			Thread.yield();
		}
	}
}

class FooEventFactory implements EventFactory<FooEvent> {
	@Override
	public FooEvent newInstance() {
		return new FooEvent(0);
	}
}

class FooWorkHandler implements WorkHandler<FooEvent> {
	private final AtomicLong counter;
	private AtomicLong sum = new AtomicLong(0);

	FooWorkHandler(AtomicLong counter) {
		this.counter = counter;
	}

	@Override
	public void onEvent(FooEvent e) throws Exception {
		Helper.process(e);
		sum.addAndGet(e.getValue());
		counter.incrementAndGet();
	}
}

class FooEventHandler implements EventHandler<FooEvent> {
	private final AtomicLong counter;
	private AtomicLong sum = new AtomicLong(0);

	public FooEventHandler(AtomicLong counter) {
		this.counter = counter;
	}

	@Override
	public void onEvent(FooEvent e, long sequence, boolean endOfBatch) throws Exception {
		Helper.process(e);
		sum.addAndGet(e.getValue());
		counter.incrementAndGet();
	}
}

class ABQConsumer implements Runnable {
	private final ArrayBlockingQueue<FooEvent> abq;
	private final AtomicLong counter;

	ABQConsumer(ArrayBlockingQueue<FooEvent> abq, AtomicLong counter) {
		this.abq = abq;
		this.counter = counter;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				FooEvent e = abq.take();
				Helper.process(e);
				sum.addAndGet(e.getValue());
				counter.incrementAndGet();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}

	private AtomicLong sum = new AtomicLong(0);

	private void process(FooEvent fe) {
		sum.addAndGet(fe.getValue());
	}
}

