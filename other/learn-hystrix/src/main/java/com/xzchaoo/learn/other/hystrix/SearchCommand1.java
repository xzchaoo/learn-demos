package com.xzchaoo.learn.other.hystrix;


import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import rx.Observable;

/**
 * created by xzchaoo at 2017/10/25
 *
 * @author xzchaoo
 */
public class SearchCommand1 extends HystrixObservableCommand<String> {
	private final int id;

	public SearchCommand1(int id) {
		super(
			Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("g1"))
				.andCommandPropertiesDefaults(
					HystrixCommandProperties.Setter()
						//.withFallbackEnabled(true) fallback 是否可用
						.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
						//.withExecutionTimeoutEnabled(true) 默认实体rue
						//执行超时会打断线程 - 可配置
						.withExecutionTimeoutInMilliseconds(1000)//超时时间
				)
		);
		this.id = id;
	}

	private static AtomicInteger ai = new AtomicInteger(0);

	@Override
	protected Observable<String> construct() {
		int c = ai.incrementAndGet();
		System.out.println("1 有" + c + "个并发");
		return Observable.timer(id, TimeUnit.MILLISECONDS).map(x -> Integer.toString(id)).doAfterTerminate(() -> {
			int c2 = ai.decrementAndGet();
			System.out.println("2 有" + c2 + "个并发");
		});
	}

	@Override
	protected Observable<String> resumeWithFallback() {
		return Observable.just("fail");
	}
}
