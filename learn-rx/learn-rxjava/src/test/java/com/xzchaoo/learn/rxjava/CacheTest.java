package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import io.reactivex.Completable;
import io.reactivex.subjects.SingleSubject;

/**
 * @author zcxu
 * @date 2018/1/9
 */
public class CacheTest {
	@Test
	public void test() throws InterruptedException {
		SingleSubject<Boolean> ss = SingleSubject.create();
		Completable c = ss.doFinally(() -> {
			//ss=null
			System.out.println("finally");
		}).doFinally(() -> {
			System.out.println("Sdf");
		}).toCompletable()
			//.onTerminateDetach()
			.cache();

		c.subscribe(() -> {
			Thread.sleep(3000);
			System.out.println("回调1");
		});
		c.subscribe(() -> {
			Thread.sleep(3000);
			System.out.println("回调2");
		});

		new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ss.onSuccess(true);
		}).start();


		Thread.sleep(10000);
		c.subscribe(() -> {
			System.out.println("回调3");
		});
	}
}
