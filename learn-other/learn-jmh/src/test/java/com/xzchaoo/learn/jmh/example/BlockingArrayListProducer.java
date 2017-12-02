package com.xzchaoo.learn.jmh.example;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * created by xzchaoo at 2017/12/2
 *
 * @author xzchaoo
 */
public class BlockingArrayListProducer implements Producer {
	private final ArrayBlockingQueue<FooEvent> q;

	public BlockingArrayListProducer(ArrayBlockingQueue<FooEvent> q) {
		this.q = q;
	}

	@Override
	public void produce(FooEvent e) {
		q.add(e);
	}

}
