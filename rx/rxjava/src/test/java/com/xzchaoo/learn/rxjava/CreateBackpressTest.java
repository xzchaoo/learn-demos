package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * created by zcxu at 2017/11/24
 *
 * @author zcxu
 */
public class CreateBackpressTest {
	@Test
	public void test() {
		Flowable.create(se -> {
			for (int i = 0; i < 100; ++i) {
				System.out.println("发射元素" + i);
				se.onNext(i);
			}
		}, BackpressureStrategy.MISSING)
			.doOnNext(e -> {
				System.out.println("开始处理元素" + e + ", 预计需要1秒");
				Thread.sleep(1000);
			}).subscribe();
	}
}
