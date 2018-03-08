package com.xzchaoo.learn.other.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;

/**
 * created by xzchaoo at 2017/10/24
 *
 * @author xzchaoo
 */
public class FirstCommand extends HystrixCommand<String> {
	private final String name;

	//相同的组会在一起进行 汇报/警告/dashboard 同一个组里的命令可以用不同的线程池
	private static final Setter SETTER = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FirstGroup"))
		.andCommandKey(HystrixCommandKey.Factory.asKey("First"))
		.andCommandPropertiesDefaults(
			HystrixCommandProperties.Setter()
				.withCircuitBreakerEnabled(true)
				.withCircuitBreakerRequestVolumeThreshold(100)
				.withCircuitBreakerErrorThresholdPercentage(10)
				.withCircuitBreakerSleepWindowInMilliseconds(5000)
		)
		.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("FirstPool"));

	public FirstCommand(String name) {
		super(SETTER);
		this.name = name;
	}

	/**
	 * 当 run 方法抛出异常 或 怎样时, 就会调用getFallback
	 *
	 * @return
	 * @throws Exception
	 */
	@Override
	protected String run() throws Exception {
		if (name.startsWith("xzc")) {
			throw new UnsupportedOperationException();
		}
		return "hello, " + name;
	}

	@Override
	protected String getFallback() {
		return "i am fallback " + name;
	}

	@Override
	protected String getCacheKey() {
		return super.getCacheKey();
	}
}
