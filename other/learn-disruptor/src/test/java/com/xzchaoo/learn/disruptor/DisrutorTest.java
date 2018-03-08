package com.xzchaoo.learn.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by xzchaoo at 2017/11/30
 *
 * @author xzchaoo
 */
public class DisrutorTest {
	@Test
	public void test() throws InterruptedException {
		ExecutorService es = Executors.newFixedThreadPool(4);
		LongEventFactory lef = new LongEventFactory();
		int bufferSize = 1024;
		Disruptor<LongEvent> d = new Disruptor<>(lef, bufferSize, es, ProducerType.SINGLE, new BlockingWaitStrategy());
		d.handleEventsWith(new LongEventHandler())
		.then((EventHandler<LongEvent>) (event, sequence, endOfBatch) -> event.clear());
		RingBuffer<LongEvent> rb = d.start();
		LongEventProducer lep = new LongEventProducer(rb);
		for (int i = 0; i < 100; ++i) {
			lep.onData(i);
			Thread.sleep(100);
		}

		es.shutdown();
	}
}
