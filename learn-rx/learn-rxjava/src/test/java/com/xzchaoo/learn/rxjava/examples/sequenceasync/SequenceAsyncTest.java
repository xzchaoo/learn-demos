package com.xzchaoo.learn.rxjava.examples.sequenceasync;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * @author xzchaoo
 * @date 2018/1/3
 */
public class SequenceAsyncTest {

	//现在有N个步骤, 每个步骤都是异步的, 每个步骤互相独立, 最多允许4个并发, 等全部结果执行完之后 对全部的结果做一个处理, 一旦有一个步骤处理失败 那么整个就失败
	@Test
	public void test2() {
		List<Integer> values = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> result = Flowable.fromIterable(values)
			.flatMapSingle(x -> Single.fromCallable(() -> {
				System.out.println("我在对" + x + "做异步处理 结果是" + (x * x));
				return x * x;
			}).delay(1, TimeUnit.SECONDS), true, 4)
			.toList()
			.blockingGet();
		System.out.println(result);

		result = Flowable.fromIterable(values)
			.concatMapEager(x -> {
				System.out.println("我在对" + x + "做异步处理 结果是" + (x * x));
				return Flowable.just(x * x).delay(1, TimeUnit.SECONDS);
			}, 4, 1)
			.toList()
			.blockingGet();
		System.out.println(result);
	}

	//现在有N个步骤, 每个步骤都是异步的, 第i+1个步骤依之前i个步骤的结果, 第1个步骤依赖一个初始值
	@Test
	public void test1() {
		List<Integer> values = Single.just(0)
			.flatMap(firstValue -> Single.fromCallable(() -> {
				List<Integer> integers = new ArrayList<>();
				integers.add(firstValue + 1);
				return integers;
			}))
			.flatMap(integers -> Single.fromCallable(() -> {
				integers.add(2);
				return integers;
			}))
			.blockingGet();
		System.out.println(values);
	}
}
