package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 测试是否线程安全
 * created by xzchaoo at 2017/11/16
 *
 * @author xzchaoo
 */
public class ThreadSafeTest {
	@Test
	public void test_publishSubject() throws InterruptedException {
		AtomicInteger ai = new AtomicInteger(0);
		Subject<Integer> ps = PublishSubject.<Integer>create()
			.toSerialized();//加了这个之后才是线程安全的
		new Thread(() -> {
			try {
				Thread.sleep(1000);
//				ps.onNext(1);
//				ps.onComplete();
				ExecutorService es = Executors.newFixedThreadPool(12);
				for (int i = 0; i < 12; ++i) {
					es.execute(() -> {
						for (int j = 0; j < 10000; ++j) {
							int c = ai.incrementAndGet();
							ps.onNext(c);
						}
					});
				}
				es.shutdown();
				es.awaitTermination(1, TimeUnit.MINUTES);
				ps.onComplete();
			} catch (Exception e) {

			}
		}).start();
		//ps.publish().autoConnect().blockingForEach(System.out::println);
		ps.blockingForEach(System.out::println);
		Thread.sleep(2000);
	}

	@Test
	public void test_flowable() throws Exception {
		//这是线程安全的
		AtomicInteger ai = new AtomicInteger(0);
		AtomicInteger ai2 = new AtomicInteger(0);
		Flowable.create(new FlowableOnSubscribe<Integer>() {
			@Override
			public void subscribe(FlowableEmitter<Integer> e) throws Exception {
				ExecutorService es = Executors.newFixedThreadPool(12);
				for (int i = 0; i < 12; ++i) {
					es.execute(() -> {
						for (int j = 0; j < 1000; ++j) {
							int c = ai.incrementAndGet();
							e.onNext(c);
						}
					});
				}
				es.shutdown();
				es.awaitTermination(1, TimeUnit.MINUTES);
				e.onComplete();
			}
		}, BackpressureStrategy.ERROR)
			.subscribeOn(Schedulers.io())
			.subscribe(e -> {
				System.out.println("ai2=" + ai2.incrementAndGet());
			});
		Thread.sleep(20000);
	}
}
