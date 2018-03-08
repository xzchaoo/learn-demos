package com.xzchaoo.learn.other.hystrix;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * created by xzchaoo at 2017/10/24
 *
 * @author xzchaoo
 */
public class App {
	@Test
	public void test1() {
		HystrixRequestContext context = HystrixRequestContext.initializeContext();

		//HystrixBadRequestException 表示请求错误 此时不会重试

		//同步执行
		//支持rxjava
		for (int i = 0; i < 28; ++i) {
			System.out.println(i + " " + new FirstCommand("xzc").execute());
		}
		System.out.println(new FirstCommand("xz222").execute());
		System.out.println(new ErrorBackToCallerCommand().execute());

		context.shutdown();
//		System.out.println(s);
//
//		System.out.println(new FirstCommand("xzc").observe().toBlocking().first());
//
//		System.out.println(new FirstObservableCommand().construct().toBlocking().first());

		//这是一个hot Observable 会马上执行
		// Observable<String> s = new FirstCommand("Bob").observe();
		//这是一个cold Observable 会等到订阅的时候才执行
		// Observable<String> s = new FirstCommand("Bob").toObservable();
	}

	@Test
	public void test_SleepCommand() throws InterruptedException {
		Random r = new Random();
		AtomicInteger successCunt = new AtomicInteger(0);
		AtomicInteger errorCount = new AtomicInteger(0);
		int threads = 4;
		ExecutorService es = Executors.newFixedThreadPool(threads);
		AtomicInteger id = new AtomicInteger(0);
		for (int k = 0; k < threads; ++k) {
			es.execute(() -> {
				while (!Thread.currentThread().isInterrupted()) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						break;
					}
					int mills = 1000 + r.nextInt(1000);
					SleepCommand sc = new SleepCommand(mills, id.incrementAndGet());
					sc.execute();
				}
			});
		}
		Thread.sleep(60000);
		es.shutdownNow();
	}

	@Test
	public void test_SearchCommand1() throws InterruptedException {
		Random r = new Random();
		for (int i = 0; i < 100; ++i) {
			int mills = 100 + r.nextInt(500);
			SearchCommand1 sc1 = new SearchCommand1(mills);
			sc1.observe().subscribe((e) -> {
				System.out.println(e);
			});
		}
		Thread.sleep(2000);
		for (int i = 0; i < 100; ++i) {
			int mills = 100 + r.nextInt(500);
			SearchCommand1 sc1 = new SearchCommand1(mills);
			sc1.observe().subscribe((e) -> {
				System.out.println(e);
			});
		}
		Thread.sleep(2000);
	}
}
