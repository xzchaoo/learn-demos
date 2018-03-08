/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.rxjava.exmaple;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class FooTest {
	private void sleepUntil(Disposable d) {
		while (!d.isDisposed()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	@Test
	public void test2() throws InterruptedException {
		//一共有10个元素
		long begin = System.currentTimeMillis();
		Disposable d = Flowable.range(0, 10)
			.flatMapSingle(e -> {
				//每个元素需要处理1秒
				//return Single.just(e).delay(1000, TimeUnit.MILLISECONDS);
				return Single.timer(1, TimeUnit.SECONDS).map(ignore -> e);
				//最多允许4个 S 并发对外发射
			}, true, 4)
			.doOnNext(System.out::println)
			.toList()
			.subscribe(list -> {
				System.out.println("我是全部的结果 " + list);
			});
		sleepUntil(d);
		System.out.println("耗时=" + (System.currentTimeMillis() - begin));
	}

}
