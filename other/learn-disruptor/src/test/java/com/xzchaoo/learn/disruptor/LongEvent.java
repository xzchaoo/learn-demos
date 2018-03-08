package com.xzchaoo.learn.disruptor;

/**
 * created by xzchaoo at 2017/11/30
 *
 * @author xzchaoo
 */
public class LongEvent {
	private long value;

	public void set(long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "LongEvent{" +
			"value=" + value +
			'}';
	}

	public void clear() {
		//释放复杂对象的引用
		this.value = 0;
	}
}
