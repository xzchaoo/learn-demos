package com.xzchaoo.learn.jmh.example;

/**
 * created by xzchaoo at 2017/12/2
 *
 * @author xzchaoo
 */
public class DirectProducer implements Producer {
	private final Consumer consumer;

	public DirectProducer(Consumer consumer) {
		this.consumer = consumer;
	}

	@Override
	public void produce(FooEvent e) {
		consumer.consume(e);
	}
}
