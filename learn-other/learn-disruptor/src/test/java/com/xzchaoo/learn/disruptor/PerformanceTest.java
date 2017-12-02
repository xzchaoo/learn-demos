package com.xzchaoo.learn.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import org.junit.Test;

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

public class PerformanceTest {
	private int n = 1000000;

	@Test
	public void test1() throws InterruptedException {
		AtomicLong sum = new AtomicLong(0);
		AtomicLong counter = new AtomicLong(0);
		long begin = System.currentTimeMillis();
		for (int i = 0; i < n; ++i) {
			FooEvent e = new FooEvent(i);
			Helper.process(e);
			sum.addAndGet(e.getValue());
			counter.incrementAndGet();
		}
		System.out.println(System.currentTimeMillis() - begin);
	}

	@Test
	public void test_abq() throws InterruptedException {
		for (int k = 0; k < 100; ++k) {
			ArrayBlockingQueue<FooEvent> abq = new ArrayBlockingQueue<FooEvent>(10000);
			ABQConsumer consumer = new ABQConsumer(abq, new AtomicLong());
			int threads = 6;
			ExecutorService es = Executors.newFixedThreadPool(threads);
			for (int i = 0; i < threads; ++i) {
				es.execute(consumer);
			}
			Thread.sleep(500);

			//Map<FooEvent, Long> produceAt = new HashMap<>();
			long begin = System.currentTimeMillis();
			for (int i = 0; i < n; ++i) {
				FooEvent e = new FooEvent(i);
				//long produceAtMills = System.currentTimeMillis();
				abq.put(e);
				//produceAt.put(e, produceAtMills);
			}
			while (!abq.isEmpty()) {
				Thread.yield();
			}
			long end = System.currentTimeMillis();
			System.out.println(end - begin);
			es.shutdownNow();
		}
	}

	@Test
	public void test_disruptor() throws Exception {
		for (int k = 0; k < 100; ++k) {
			ExecutorService es = Executors.newFixedThreadPool(6);
			FooEventFactory lef = new FooEventFactory();
			int bufferSize = 1024;
			Disruptor<FooEvent> d = new Disruptor<>(lef, bufferSize, es, ProducerType.SINGLE, new BlockingWaitStrategy());
			AtomicLong counter = new AtomicLong();
			d.handleEventsWith(new FooEventHandler(counter));
			RingBuffer<FooEvent> rb = d.start();
			long begin = System.currentTimeMillis();
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
			long end = System.currentTimeMillis();
			System.out.println(end - begin);
			d.shutdown();
			es.shutdownNow();
		}
	}
}

class FooEventFactory implements EventFactory<FooEvent> {
	@Override
	public FooEvent newInstance() {
		return new FooEvent(0);
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

