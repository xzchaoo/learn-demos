package com.xzchaoo.learn.jmh;

import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * created by xzchaoo at 2017/12/3
 *
 * @author xzchaoo
 */
@BenchmarkMode({Mode.Throughput,Mode.AverageTime})
@Measurement(iterations = 3)
@Warmup(iterations = 2)
@Fork(1)
public class ForkJoinTest {
	@State(Scope.Benchmark)
	public static class FooState {
		int n = 1000000;
		List<Double> list;

		@Setup
		public void setup() {
			list = new ArrayList<>(n);
			Random r = new Random();
			for (int i = 0; i < n; ++i) {
				list.add(r.nextDouble());
			}
		}
	}

	public static double calc(double x) {
		for (int i = 0; i < 1; ++i) {
			x = Math.sqrt(x);
		}
		return x;
	}

	@Benchmark
	public void singleThread(FooState state, Blackhole b) {
		int n = state.n;
		List<Double> list = state.list;
		int count = 0;
		for (int i = 0; i < n; ++i) {
			double x = list.get(i);
			if (calc(x) > 0.5) {
				++count;
			}
		}
		b.consume(count);
	}

	@Benchmark
	public void singleThread2(FooState state, Blackhole b) {
		List<Double> list = state.list;
		int count = (int) list.stream()
			.filter(x -> calc(x) > 0.5)
			.count();
		b.consume(count);
	}

	@Benchmark
	public void rxjava1(FooState state, Blackhole b) {
		Long r = Flowable.fromIterable(state.list)
			.filter(x -> calc(x) > 0.5)
			.count()
			.blockingGet();
		b.consume(r);
	}

	@Benchmark
	@Measurement(iterations = 10, time = 5)
	@Warmup(iterations = 10, time = 5)
	public void rxjava2(FooState state, Blackhole b) throws InterruptedException {
		int threads = 10;
		Subscriber<Double>[] ss = new Subscriber[threads];
		AtomicLong sum = new AtomicLong(0);
		CountDownLatch cdl = new CountDownLatch(threads);
		for (int i = 0; i < threads; ++i) {
			ss[i] = new Subscriber<Double>() {
				AtomicInteger longAdder = new AtomicInteger();

				@Override
				public void onSubscribe(Subscription s) {
					s.request(Long.MAX_VALUE);
				}

				@Override
				public void onNext(Double x) {
					if (calc(x) > 0.5) {
						longAdder.incrementAndGet();
					}
				}

				@Override
				public void onError(Throwable t) {

				}

				@Override
				public void onComplete() {
					sum.addAndGet(longAdder.longValue());
					cdl.countDown();
				}
			};
		}
		//Flowable.fromIterable(state.list)
		Flowable.fromIterable(state.list)
			.parallel(threads)
			.runOn(Schedulers.computation())
			.subscribe(ss);
		while (cdl.getCount() != 0) {
			Thread.yield();
		}
	}

	@Benchmark
	@Measurement(iterations = 10)
	@Warmup(iterations = 10)
	public void rxjava3(FooState state, Blackhole b) {
		Integer r = Flowable.fromIterable(state.list)
			.parallel(10)
			.runOn(Schedulers.computation())
			.filter(x -> calc(x) < 0.5)
			.reduce(() -> 0, (s, x) -> s + 1)
			.sequential()
			.reduce(0, (s, x) -> s + x)
			.blockingGet();
		b.consume(r);
	}

	@Benchmark
	@Measurement(iterations = 10)
	@Warmup(iterations = 10)
	public void d(FooState state, DState d, Blackhole b) {
		RingBuffer<FJEvent> rb = d.rb;
		AtomicLong c = d.counter;
		c.set(0);
		int n = state.n;
		for (int i = 0; i < n; ++i) {
			long sq = rb.next();
			try {
				FJEvent e = rb.get(sq);
				e.d = state.list.get(i);
			} finally {
				rb.publish(sq);
			}
		}
		while (c.get() != n) {
			Thread.yield();
		}
	}

	@State(Scope.Benchmark)
	public static class DState {
		int threads = 10;
		ExecutorService es;
		int bufferSize = 131072;
		FJEventFactory lef = new FJEventFactory();
		private Disruptor<FJEvent> d;
		private AtomicLong counter;
		private RingBuffer<FJEvent> rb;

		@Setup
		public void setup() {
			es = Executors.newFixedThreadPool(threads);
			d = new Disruptor<>(lef, bufferSize, es, ProducerType.SINGLE, new BusySpinWaitStrategy());
			counter = new AtomicLong();

			WorkHandler[] workers = new WorkHandler[threads];
			for (int i = 0; i < threads; ++i) {
				workers[i] = new FooWorkHandler2(counter);
			}
			d.handleEventsWithWorkerPool(
				workers
			);

			rb = d.start();
		}

		@TearDown
		public void td() {
			d.shutdown();
			es.shutdownNow();
		}
	}
}

class FJEventFactory implements EventFactory<FJEvent> {

	@Override
	public FJEvent newInstance() {
		return new FJEvent();
	}
}

class FJEvent {
	public double d;
}

class FooWorkHandler2 implements WorkHandler<FJEvent> {
	private final AtomicLong counter;
	private final AtomicLong acounter = new AtomicLong(0);

	public FooWorkHandler2(AtomicLong counter) {
		this.counter = counter;
	}

	@Override
	public void onEvent(FJEvent e) throws Exception {
		if (ForkJoinTest.calc(e.d) > 0.5) {
			acounter.incrementAndGet();
		}
		counter.incrementAndGet();
	}
}