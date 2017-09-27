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

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CreateOperatorTest {
	@Test
	public void range() {
		Observable.range(0, 10).blockingForEach(System.out::println);
	}

	@Ignore
	@Test
	public void interval() {
		//这个方法永远不结束
		Observable.interval(1, TimeUnit.SECONDS).blockingForEach(System.out::println);

		//timer则是只会触发一次
		//Observable.timer(1,TimeUnit.SECONDS).blockingForEach(System.out::println);
	}

	@Test
	public void from() throws InterruptedException {
		//System.out.println(Thread.currentThread());
		Observable.fromCallable(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				System.out.println(Thread.currentThread());
				return 1;
			}
		}).subscribeOn(Schedulers.newThread())
			.observeOn(Schedulers.computation())
			.subscribe(e -> {
				System.out.println(Thread.currentThread());
				System.out.println(e);
			});
		//System.out.println(Thread.currentThread());
		Thread.sleep(2000);
	}

	@Test
	public void create() {
		Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> e) throws Exception {
				for (int i = 0; i < 3; ++i) {
					e.onNext(i);
				}
				e.onComplete();
				e.onComplete();
				//e.onError(new RuntimeException());
				System.out.println(e.tryOnError(new RuntimeException()));
			}
		}).subscribe(new Observer<Integer>() {
			@Override
			public void onSubscribe(Disposable d) {
				System.out.println("onSubscribe");
			}

			@Override
			public void onNext(Integer integer) {
				System.out.println("onNext " + integer);
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onComplete() {
				System.out.println("complete");
			}
		});
	}

	@Test
	public void empty() {
		Observable.<Void>empty().subscribe(new Observer<Void>() {
			@Override
			public void onSubscribe(Disposable d) {
				System.out.println("onSubscribe");
			}

			@Override
			public void onNext(Void aVoid) {
				System.out.println("impossible");
			}

			@Override
			public void onError(Throwable e) {
				System.out.println("impossible");
			}

			@Override
			public void onComplete() {
				System.out.println("complete");
			}
		});
	}

	@Test
	public void just() {
		Observable.just(1).subscribe((t) -> {
			System.out.println(t);
		}, e -> {
		}, () -> {
			System.out.println("end");
		});
	}
}
