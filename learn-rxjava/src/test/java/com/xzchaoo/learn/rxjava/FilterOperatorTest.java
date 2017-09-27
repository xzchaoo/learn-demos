/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class FilterOperatorTest {
	@Test
	public void test() {
		Observable.interval(100, TimeUnit.MILLISECONDS)
			.take(3)
			//但是对于最后一个元素 就不会 debounce 了
			.debounce(new Function<Long, ObservableSource<Long>>() {
				@Override
				public ObservableSource<Long> apply(Long aLong) throws Exception {
					//当 返回的O complete 的时候就会发射出最近一个元素
					System.out.println("apply " + aLong);
					return Observable.timer(5000, TimeUnit.MILLISECONDS);
				}
			})
			.sample(60, TimeUnit.MILLISECONDS, true)
			.blockingForEach(System.out::println);
		//表示每60毫秒最多发射一个元素 emitLast=true 表示要发射最后一个元素

	}
}
