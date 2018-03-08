package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zcxu
 * @date 2018/1/17
 */
public class LifecycleTest {
	@Test
	public void test() throws InterruptedException {
		AtomicInteger ai = new AtomicInteger();
		Single.<String>create(se -> {
			Thread.sleep(100);
			se.onSuccess("1");
		}).subscribeOn(Schedulers.io())
			.doFinally(() -> {
				System.out.println(ai.incrementAndGet());
				Thread.sleep(1000);
				System.out.println(ai.decrementAndGet());
				//observeOn是异步的 导致上面的s被认为是结束 因此finally会执行
			}).observeOn(Schedulers.computation())
			.doOnSuccess(ignore -> {
				System.out.println(ai.incrementAndGet());
				Thread.sleep(1000);
				System.out.println(ai.decrementAndGet());
			})
			.subscribe();
		Thread.sleep(1000000);
	}
}
