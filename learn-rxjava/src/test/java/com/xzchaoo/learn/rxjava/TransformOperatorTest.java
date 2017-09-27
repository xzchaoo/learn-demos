/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.rxjava;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class TransformOperatorTest {
	private Observable<Integer> o;

	@Before
	public void before() {
		o = Observable.range(1, 5);
	}

	@Test
	public void flatMap() {
		Long size = Observable.range(1, 10).flatMap(x -> {
			//将1映射成 大小为x的数组 数组内容全都是1
			List<Integer> list = new ArrayList<>(x);
			for (int i = 0; i < x; ++i) {
				list.add(1);
			}
			return Observable.fromIterable(list);
		}, (e) -> Observable.just(-1), () -> Observable.just(-2)).count().blockingGet();
		//如果遇到错误就映射成-1 如果遇到结束就映射成-2 所以结果是 55 + 1 = 56个
		System.out.println(size);//56
	}

	@Test
	public void buffer() {
		//每2个作为一组 进行发射 收到的是一个 List<Integer> 大小是2 最后一组大小是1
		o.buffer(2).blockingForEach(System.out::println);

		//可以先跳过1个
		o.buffer(3, 1).blockingForEach(System.out::println);

		Observable.interval(100, TimeUnit.MILLISECONDS)
			.take(10)
			.buffer(310, TimeUnit.MILLISECONDS)
			.blockingForEach(System.out::println);


		Observable.interval(100, TimeUnit.MILLISECONDS)
			.take(10)
			.buffer(Observable.interval(310, TimeUnit.MILLISECONDS))
			.blockingForEach(System.out::println);

		System.out.println("timespan + count");
		Observable.interval(1, TimeUnit.MILLISECONDS)
			.take(10)
			.buffer(310, TimeUnit.MILLISECONDS, 2)
			.blockingForEach(x -> {
				System.out.println(System.currentTimeMillis());
				System.out.println(x);
			});
	}

	@Test
	public void group() {
		//分组之后每个组是一个observable
		o.groupBy(x -> x % 2).blockingForEach(go -> {
			System.out.println("key=" + go.getKey());
			go.blockingForEach(System.out::println);
		});
	}

	@Test
	public void map() {
		o.map(x -> "X" + Integer.toString(x)).blockingForEach(System.out::println);
	}
}
