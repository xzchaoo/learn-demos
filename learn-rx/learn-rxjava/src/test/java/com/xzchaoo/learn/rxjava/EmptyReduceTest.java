package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import io.reactivex.Flowable;

/**
 * @author zcxu
 * @date 2017/12/12
 */
public class EmptyReduceTest {
	@Test
	public void test() {
		System.out.println(Flowable.<Integer>empty().reduce(3, (a, b) -> a + b).blockingGet());
	}
}
