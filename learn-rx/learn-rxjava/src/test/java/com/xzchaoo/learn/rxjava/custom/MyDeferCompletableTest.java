package com.xzchaoo.learn.rxjava.custom;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.completable.CompletableEmpty;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zcxu
 * @date 2018/1/17
 */
public class MyDeferCompletableTest {

	@Test
	public void test_MyDeferCompletable() throws InterruptedException {
		long begin = System.currentTimeMillis();
		Completable c = new MyDeferCompletable(CompletableEmpty.INSTANCE, 1, TimeUnit.SECONDS, Schedulers.io());
		Disposable d = c.doOnComplete(() -> {
			System.out.println("complete!");
		}).subscribe();
		Thread.sleep(100);
		d.dispose();
		Thread.sleep(2000);
		System.out.println("耗时=" + (System.currentTimeMillis() - begin));
	}
}
