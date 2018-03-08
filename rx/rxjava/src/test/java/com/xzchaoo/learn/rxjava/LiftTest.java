package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOperator;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * list操作符是干嘛的?
 * created by zcxu at 2017/12/7
 *
 * @author zcxu
 */
public class LiftTest {
	@Test
	public void test() {
		Single<Integer> s = Single.timer(5, TimeUnit.SECONDS, Schedulers.computation())
			.map(Long::intValue)
			.observeOn(Schedulers.computation());
		s.lift(new SingleOperator<Integer, Integer>() {
			@Override
			public SingleObserver<Integer> apply(SingleObserver<? super Integer> singleObserver) throws Exception {
				return new SingleObserver<Integer>() {
					@Override
					public void onSubscribe(Disposable d) {
						System.out.println("这里1 " + Thread.currentThread());
						singleObserver.onSubscribe(d);
					}

					@Override
					public void onSuccess(Integer integer) {
						System.out.println("这里2 " + Thread.currentThread());
						singleObserver.onSuccess(integer);
					}

					@Override
					public void onError(Throwable e) {
						singleObserver.onError(e);
					}
				};
			}
		}).subscribeOn(Schedulers.io())
			.lift(new SingleOperator<Object, Integer>() {
				@Override
				public SingleObserver<? super Integer> apply(SingleObserver<? super Object> observer) throws Exception {
					return new SingleObserver<Integer>() {
						@Override
						public void onSubscribe(Disposable d) {
							System.out.println("这里3 " + Thread.currentThread());
							observer.onSubscribe(d);
						}

						@Override
						public void onSuccess(Integer integer) {
							observer.onSuccess(integer);
						}

						@Override
						public void onError(Throwable e) {
							observer.onError(e);
						}
					};
				}
			})
			.doOnSuccess(System.out::println)
			.blockingGet();
	}
}
