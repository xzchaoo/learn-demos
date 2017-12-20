package com.xzchaoo.learn.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * created by xzchaoo at 2017/11/30
 *
 * @author xzchaoo
 */
public class LongEventProducer {
	private final RingBuffer<LongEvent> rb;

	public LongEventProducer(RingBuffer<LongEvent> rb) {
		this.rb = rb;
	}

	private static final EventTranslatorOneArg<LongEvent, Long> TRANSLATOR =
		(event, sequence, data) -> event.set(data);


	public void onData(long data) {
		rb.publishEvent(TRANSLATOR, data);
//		rb.publishEvent(new EventTranslator<LongEvent>() {
//			@Override
//			public void translateTo(LongEvent event, long sequence) {
//				event.set(data);
//			}
//		});

//		long sequence = rb.next();
//		try {
//			LongEvent e = rb.get(sequence);
//			e.set(data);
//		} finally {
//			rb.publish(sequence);
//		}
	}
}
