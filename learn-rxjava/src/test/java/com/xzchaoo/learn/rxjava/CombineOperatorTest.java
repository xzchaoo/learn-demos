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
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

public class CombineOperatorTest {
	@Test
	public void join() throws InterruptedException {
		Observable<Long> o1 = Observable.interval(100, TimeUnit.MILLISECONDS).take(10);
		Observable<Long> o2 = Observable.interval(300, TimeUnit.MILLISECONDS).take(10);

		//会等到最后一个元素结束
		o1.join(o2, new Function<Long, ObservableSource<String>>() {
			@Override
			public ObservableSource<String> apply(Long aLong) throws Exception {
				return Observable.empty();
			}
		}, new Function<Long, ObservableSource<String>>() {
			@Override
			public ObservableSource<String> apply(Long aLong) throws Exception {
				return Observable.empty();
			}
		}, new BiFunction<Long, Long, String>() {
			@Override
			public String apply(Long a, Long b) throws Exception {
				return a.toString() + b.toString();
			}
		}).forEach(System.out::println);
		//Thread.sleep(6000000);
	}

	@Test
	public void switch1() {
		Observable<Long> o1 = Observable.interval(1, TimeUnit.SECONDS);
		Observable<Long> o2 = Observable.interval(100, TimeUnit.MILLISECONDS).take(10);

		Observable.switchOnNextDelayError(
			Observable.intervalRange(0, 2, 0, 5, TimeUnit.SECONDS).map(x -> {
				return x == 0 ? o1 : o2;
			})
		).blockingForEach(System.out::println);
	}

	@Test
	public void concat() {
		Observable.concat(
			Observable.just(1, 2, 3),
			Observable.just(4, 5, 6)
		).blockingForEach(System.out::println);
	}

	@Test
	public void zip() {
		Observable<Integer> o1 = Observable.just(1, 2, 3);
		Observable<Integer> o2 = Observable.just(-1, -2, -3, -4);//-4 is ignored
		Observable.zip(o1, o2, (a, b) -> a.toString() + b.toString()).blockingForEach(System.out::println);
	}
}
