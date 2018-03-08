package com.xzchaoo.learn.jmh.example;

/**
 * created by xzchaoo at 2017/12/2
 *
 * @author xzchaoo
 */
public class FooEvent {
	private long value;

	public FooEvent() {
	}

	public FooEvent(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
}

