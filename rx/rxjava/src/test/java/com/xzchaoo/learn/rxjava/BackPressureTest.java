/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.rxjava;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BackPressureTest {
	@Ignore
	@Test
	public void test() throws InterruptedException {
		Single.just(1).subscribe(new SingleObserver<Integer>() {
			@Override
			public void onSubscribe(Disposable d) {

			}

			@Override
			public void onSuccess(Integer integer) {

			}

			@Override
			public void onError(Throwable e) {

			}
		});
		AtomicInteger count1 = new AtomicInteger(0);
		AtomicInteger count = new AtomicInteger(0);
		Flowable.range(1, 100)
			.flatMap(x -> {
				return Flowable.intervalRange(1, 200000, 1, 1, TimeUnit.MILLISECONDS).doOnSubscribe(ignore -> {
					System.out.println("开始订阅 " + x);
				}).map(y -> count1.incrementAndGet())
					.doOnNext(y -> {
						System.out.println("实际产生 " + y);
					});
			}, false, 2)
			.map(x -> {
				System.out.println("产生 " + x);
				return x;
			})
			.observeOn(Schedulers.io())
			.subscribe(x -> {
				System.out.println("开始处理 " + x);
				Thread.sleep(50);
				System.out.println("结束处理 " + count.incrementAndGet());
			}, e -> {
				e.printStackTrace();
			});
		Thread.sleep(100000);
	}
}
