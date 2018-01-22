package com.xzchaoo.learn.rxjava.custom;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zcxu
 * @date 2018/1/17
 */
public class MySingleTimerTest {
	@Test
	public void test() throws InterruptedException {
		long begin = System.currentTimeMillis();
		Single<Long> s = new MySingleTimer(1, TimeUnit.SECONDS, Schedulers.single());
		Disposable d = s.subscribe(System.out::println);
		d.dispose();
		Thread.sleep(2000);
	}
}
