package com.xzchaoo.learn.rxjava.misc;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;

/**
 * created by xzchaoo at 2017/12/20
 *
 * @author xzchaoo
 */
public class MiscTest {
	@Test
	public void test() throws InterruptedException {
		Flowable.generate(() -> 0, new BiConsumer<Integer, Emitter<Integer>>() {
			@Override
			public void accept(Integer o, Emitter<Integer> emitter) throws Exception {
				emitter.onNext(o);
			}
		}).observeOn(Schedulers.io(), true, 1)
			.flatMap(ignore -> {
//				try {
//					return Flowable.create(se -> {
//						Thread.sleep(1000);
//						System.out.println("生成了10个元素");
//						for (int i = 0; i < 10; ++i) {
//							se.onNext(i);
//						}
//						se.onComplete();
//					}, BackpressureStrategy.BUFFER);
//				} finally {
				System.out.println("生成0");
//				}
//				return Flowable.interval(1, TimeUnit.MILLISECONDS).take(10).toList().doOnSuccess(ignore2 -> {
//					System.out.println("生成了10个元素");
//				}).flatMapPublisher(Flowable::fromIterable);
				//});
				return Flowable.interval(1, TimeUnit.MILLISECONDS).take(10).onBackpressureBuffer();
			}, true, 2)
			.flatMapSingle(e -> {
				return Single.timer(1, TimeUnit.SECONDS)
					.doOnSuccess(ignore -> {
						System.out.println("处理了一个元素");
					});
			}, true, 2)
			.subscribe();
		System.out.println("主线程");
		Thread.sleep(9999999);
	}
}
