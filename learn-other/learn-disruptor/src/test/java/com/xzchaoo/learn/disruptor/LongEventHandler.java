package com.xzchaoo.learn.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * created by xzchaoo at 2017/11/30
 *
 * @author xzchaoo
 */
public class LongEventHandler implements EventHandler<LongEvent> {
	@Override
	public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
		System.out.println("LongEvent: " + event);
	}
}
