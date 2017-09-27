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

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

public class DisposeTest {
	@Ignore
	@Test
	public void test_dispose() throws InterruptedException {
		Disposable d = Flowable.interval(1, TimeUnit.SECONDS)
			.take(1)
			.subscribe(System.out::println);
		System.out.println(d.isDisposed());
		for (int i = 0; i < 100; ++i) {
			Thread.sleep(1000);
			System.out.println(d.isDisposed());
			d.dispose();
		}
	}
}
