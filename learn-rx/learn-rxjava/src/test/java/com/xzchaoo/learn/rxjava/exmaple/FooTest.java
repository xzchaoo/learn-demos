/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.rxjava.exmaple;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class FooTest {
	@Ignore
	@Test
	public void test2() throws InterruptedException {
		Flowable.range(0, 10)
			.flatMap(e -> {
				return Flowable.interval(1000, TimeUnit.MILLISECONDS).map(x -> e);
				//最多允许4个 F 并发对外发射
			}, true, 4)
			.doOnNext(e -> {
				System.out.println(e);
			})
			.subscribe();
		Thread.sleep(100000);
	}
	@Ignore
	@Test
	public void test() throws InterruptedException {
		Flowable.range(0, 10)
			.flatMapSingle(e -> {

				return Single.just(e).observeOn(Schedulers.io()).doOnSuccess(e2 -> {
					System.out.println("开始处理");
					Thread.sleep(5000);
					System.out.println("结束处理");
				});

			}, true, 4)
			.subscribe();
		Thread.sleep(100000);
	}
}
