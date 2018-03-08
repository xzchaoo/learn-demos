package com.xzchaoo.learn.other.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * created by zcxu at 2017/10/25
 *
 * @author zcxu
 */
public class SleepCommand extends HystrixCommand<String> {
	private final int mills;
	private final int id;

	public SleepCommand(int mills, int id) {
		super(
			Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("g1"))
				.andCommandPropertiesDefaults(
					HystrixCommandProperties.Setter()
						//.withFallbackEnabled(true) fallback 是否可用
						.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
						//.withExecutionTimeoutEnabled(true) 默认实体rue
						//执行超时会打断线程 - 可配置
						.withExecutionTimeoutInMilliseconds(1000)//超时时间
				)
				.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("p1"))
				.andThreadPoolPropertiesDefaults(
					HystrixThreadPoolProperties.Setter()
						.withCoreSize(10)
						.withMaximumSize(10)
						.withAllowMaximumSizeToDivergeFromCoreSize(true)
						.withMaxQueueSize(-1)
				)
		);
		this.mills = mills;
		this.id = id;
	}

	@Override
	protected String run() throws Exception {
		System.out.println(System.currentTimeMillis() + " 开始睡觉 " + id);
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw e;
		}
		System.out.println("结束睡觉");
		return Integer.toString(mills);
	}

	@Override
	protected String getFallback() {
		return "i am fallback";
	}
}
