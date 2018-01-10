package com.xzchaoo.learn.jdk8.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/3/21.
 */
public class FutureApp {
	public static void main(String[] args) throws Exception {
		ExecutorService es = Executors.newFixedThreadPool(2);

		Future<Void> f = es.submit(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				System.out.println("begin");
				Thread.sleep(1000);
				System.out.println("end");
				return null;
			}
		});
		System.out.println(f.get());
		es.shutdown();
		es.awaitTermination(1, TimeUnit.DAYS);
	}
}
