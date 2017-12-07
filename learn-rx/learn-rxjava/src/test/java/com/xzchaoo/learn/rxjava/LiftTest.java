package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOperator;
import io.reactivex.subjects.SingleSubject;

/**
 * list操作符是干嘛的?
 * created by zcxu at 2017/12/7
 *
 * @author zcxu
 */
public class LiftTest {
	@Test
	public void test() {
		Single<Integer> s = Single.just(1);
		s.lift(new SingleOperator<Integer, Integer>() {
			@Override
			public SingleObserver<Integer> apply(SingleObserver<? super Integer> singleObserver) throws Exception {
				SingleSubject<Integer> ss = SingleSubject.create();
				ss.subscribe(singleObserver);
				return ss;
			}
		}).doOnSuccess(System.out::println)
			.subscribe();
	}
}
