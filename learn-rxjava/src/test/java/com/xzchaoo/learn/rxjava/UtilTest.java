/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.rxjava;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.SingleSubject;

public class UtilTest {
	@Test
	public void test_SingleSubject() {
		//SingleSubject 是一个 Single
		SingleSubject<Integer> ss = SingleSubject.create();
		ss.subscribe(System.out::println);
		ss.onSuccess(1);
	}

	@Test
	public void test_PublishSubject() {
		//PublishSubject 是一个 Observable
		PublishSubject<Integer> ps = PublishSubject.create();
		ps.forEach(System.out::println);
		for (int i = 0; i < 100; ++i) {
			ps.onNext(i);
		}
		ps.onComplete();
	}

	@Test
	public void test_BehaviorSubject() {
		BehaviorSubject<Integer> bs = BehaviorSubject.createDefault(-1);
		bs.forEach(System.out::println);
		for (int i = 0; i < 10; ++i) {
			bs.onNext(i);
		}
		//现在才订阅 会打印出9
		bs.forEach(System.out::println);
		bs.onComplete();
	}

	@Test
	public void test_AsyncSubject() {
		AsyncSubject<Integer> as = AsyncSubject.create();
		as.subscribe(System.out::println);
		as.onNext(1);
		as.onNext(2);
		as.onNext(3);//只有最后一个会生效
		as.onComplete();
	}

	@Test
	public void test_PublishProcessor() {
		PublishProcessor<Integer> pp = PublishProcessor.create();
		pp.forEach(System.out::println);
		for (int i = 0; i < 100; ++i) {
			pp.onNext(i);
		}
		pp.onComplete();
	}
	@Ignore
	@Test
	public void test_parallel() {
		Flowable.generate(new Consumer<Emitter<Object>>() {
			@Override
			public void accept(Emitter<Object> emitter) throws Exception {

			}
		});

		//并行流
		Flowable.range(1, 1000)
			.parallel()
			.filter(x -> x % 2 == 0)
			.map(x -> x * x)
			.sequential()
			.onBackpressureBuffer(1)
			.blockingForEach(System.out::println);
	}

	@Test
	public void batchRequests() throws InterruptedException {
		PublishSubject<Object> ps = PublishSubject.create();

		ps.subscribeOn(Schedulers.io());

		BatchRequest br0 = new BatchRequest();
		List<SubRequest> list = new ArrayList<>();
		list.add(new SubRequest());
		list.add(new SubRequest());
		br0.setSubRequests(list);

		Semaphore s = new Semaphore(1);
		long begin = System.currentTimeMillis();
		Observable.just(br0)
			.flatMap(br -> Observable.fromIterable(br.getSubRequests()))
			.flatMapSingle(sr -> {
					s.acquire();
					return Single.fromCallable(() -> {
						//send request here
						//assume we use Blocking HTTP Client
						Thread.sleep(1000);
						return "response of " + sr;
					}).subscribeOn(Schedulers.io())
						.doFinally(s::release);
				}
			)
			.flatMapSingle(response -> Single.fromCallable(() -> {
					//do compute here, the computation takes 2 seconds
					Thread.sleep(2000);
					return response;
				}).subscribeOn(Schedulers.computation())
			)
			.toList()
			.subscribe((results) -> {
				System.out.println("finish in " + (System.currentTimeMillis() - begin) + " ms");
				System.out.println("callback with " + results);
			});
		Thread.sleep(9000);

		Observable.just(br0)
			.flatMap(br -> Observable.fromIterable(br.getSubRequests()))
			.flatMapSingle(sr ->
				{
					return Single.fromCallable(() -> {
						//send request here
						//assume we use Blocking HTTP Client
						Thread.sleep(1000);
						return "response of " + sr;
					}).subscribeOn(Schedulers.io())
						.doOnError(e -> s.release())
						.doOnSuccess((e) -> s.release());
				}
			)
			.flatMapSingle(response -> Single.fromCallable(() -> {
					//do compute here, the computation takes 2 seconds
					Thread.sleep(2000);
					return response;
				}).subscribeOn(Schedulers.computation())
			)
			.toList()
			.subscribe((results) -> {
				System.out.println("finish in " + (System.currentTimeMillis() - begin) + " ms");
				System.out.println("callback with " + results);
			});
		Thread.sleep(4000);
	}

	@Test
	public void test() throws InterruptedException {
		List<Task1> tasks = new ArrayList<>();
		tasks.add(new Task1(1));
		tasks.add(new Task1(2));
		tasks.add(new Task1(3));

		//每个任务并发执行
		Observable.fromIterable(tasks).flatMap(task -> {
			//每个任务需要执行 bi 次请求
			//setup requests for this task
			return Observable.range(0, task.getRequestCount())
				.map(x -> "request " + x)
				.flatMap(request -> {
					//假设这里发出了http请求
					int delay = ThreadLocalRandom.current().nextInt(1000);
					return Observable.fromCallable(new Callable<Integer>() {
						@Override
						public Integer call() throws Exception {
							return 1;
						}
					}).delay(delay, TimeUnit.MILLISECONDS);
				}).toList()
				.map(results -> (int) results.stream().mapToInt(x -> x).average().orElse(0))
				.toObservable();
		}).toList()
			.flatMap(results -> {
				int sum = results.stream().mapToInt(x -> x).sum();
				return Single.fromCallable(() -> {
					//send email
					return "sum is " + sum;
				}).delay(2, TimeUnit.SECONDS);
			}).subscribe(System.out::println);

		Thread.sleep(10000);
	}

	@Test
	public void test_thread2() throws Exception {
		Observable.create(new ObservableOnSubscribe<Void>() {
			@Override
			public void subscribe(ObservableEmitter<Void> e) throws Exception {

			}
		});
		Observable.range(1, 100).forEach(System.out::println);
		System.out.println("end");
	}
	@Ignore
	@Test
	public void test_thread() throws Exception {
		System.out.println(Thread.currentThread());
		Observable.create(new ObservableOnSubscribe<Integer>() {
			@Override
			public void subscribe(ObservableEmitter<Integer> e) throws Exception {
				System.out.println("subscribe 1" + Thread.currentThread());
				new Thread(() -> {
					System.out.println("subscribe 2" + Thread.currentThread());
					e.onNext(1);
					System.out.println("onNext end");
					e.onComplete();
				}).start();
			}
		}).subscribeOn(Schedulers.io())
			.observeOn(Schedulers.computation())
			.map(x -> {
				Thread.sleep(2000);
				System.out.println("map1 " + Thread.currentThread());
				return x;
			})
			.map(x -> {
				System.out.println("map2 " + Thread.currentThread());
				return x;
			})
			.observeOn(Schedulers.io())
			.map(x -> {
				System.out.println("map3 " + Thread.currentThread());
				return x;
			})
			.observeOn(Schedulers.computation())
			.subscribeOn(Schedulers.newThread())
			.subscribe(new Observer<Integer>() {
				@Override
				public void onSubscribe(Disposable d) {
					System.out.println("onSubscribe " + Thread.currentThread());
				}

				@Override
				public void onNext(Integer integer) {
					System.out.println("onNext" + Thread.currentThread());
				}

				@Override
				public void onError(Throwable e) {
					e.printStackTrace();
				}

				@Override
				public void onComplete() {
					System.out.println("onComplete" + Thread.currentThread());
				}
			});

		Thread.sleep(20000);
	}

	@Test
	public void test1() throws InterruptedException {
		Scheduler s1 = Schedulers.from(Executors.newFixedThreadPool(4, new ThreadFactoryBuilder().setNameFormat("s1-%d").build()));
		Scheduler s2 = Schedulers.from(Executors.newFixedThreadPool(4, new ThreadFactoryBuilder().setNameFormat("s2-%d").build()));
		Observable.interval(1, TimeUnit.SECONDS, s1).observeOn(s2).forEach(x -> {
			System.out.println(Thread.currentThread());
			System.out.println(x);
		});
		Thread.sleep(10000);
	}
}
