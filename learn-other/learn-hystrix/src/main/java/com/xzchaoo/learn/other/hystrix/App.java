package com.xzchaoo.learn.other.hystrix;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import org.junit.Test;

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
}
