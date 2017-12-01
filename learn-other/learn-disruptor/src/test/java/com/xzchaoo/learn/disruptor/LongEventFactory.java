package com.xzchaoo.learn.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * created by xzchaoo at 2017/11/30
 *
 * @author xzchaoo
 */
public class LongEventFactory implements EventFactory<LongEvent> {
	@Override
	public LongEvent newInstance() {
		return new LongEvent();
	}
}
