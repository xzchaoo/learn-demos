package com.xzchaoo.learn.rxjava;

import org.junit.Test;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.*;

/**
 * created by xzchaoo at 2017/11/17
 *
 * @author xzchaoo
 */
public class FlowableTest {
	@Test
	public void test_merged() {
		//将两个F合并起来发射, 这个过程是线程安全的
		Flowable<Integer> f1 = Flowable.<Integer>create(se -> {
			for (int i = 0; i < 1000; ++i) {
				se.onNext(i);
				Thread.sleep(1);
			}
			se.onComplete();
		}, BackpressureStrategy.MISSING)
			.subscribeOn(Schedulers.io());
		AtomicInteger ai = new AtomicInteger(0);
		Flowable.merge(f1, f1)
			.blockingForEach(e -> {
				ai.incrementAndGet();
			});
		System.out.println(ai.get());
	}

	/**
	 * 用于分组
	 */
	@Test
	public void test_buffer_1_group() {
		List<List<Integer>> list = Flowable.just(1, 2, 3, 4, 5)
			.buffer(2)
			.toList()
			.blockingGet();
		assertEquals(3, list.size());
		assertEquals(1, list.get(0).get(0).intValue());
		assertEquals(2, list.get(0).get(1).intValue());
		assertEquals(3, list.get(1).get(0).intValue());
		assertEquals(4, list.get(1).get(1).intValue());
		assertEquals(5, list.get(2).get(0).intValue());
	}

	@Test
	public void test_buffer_2() {
		//每2个 或 每秒组成一个组 看哪个条件先达到了
		List<List<Integer>> list = Flowable.just(1, 2, 3, 4, 5)
			.buffer(1, TimeUnit.SECONDS, 2)
			.toList()
			.blockingGet();
		assertEquals(3, list.size());
		assertEquals(1, list.get(0).get(0).intValue());
		assertEquals(2, list.get(0).get(1).intValue());
		assertEquals(3, list.get(1).get(0).intValue());
		assertEquals(4, list.get(1).get(1).intValue());
		assertEquals(5, list.get(2).get(0).intValue());

		list = Flowable.interval(400, TimeUnit.MILLISECONDS)
			.map(Long::intValue)
			.map(x -> x + 1)
			.take(5)
			.buffer(1, TimeUnit.SECONDS, 3)
			.toList()
			.blockingGet();
		assertEquals(3, list.size());
		assertEquals(1, list.get(0).get(0).intValue());
		assertEquals(2, list.get(0).get(1).intValue());
		assertEquals(3, list.get(1).get(0).intValue());
		assertEquals(4, list.get(1).get(1).intValue());
		assertEquals(5, list.get(2).get(0).intValue());
	}
}
