package com.xzchaoo.learn.other.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * created by xzchaoo at 2017/10/25
 *
 * @author xzchaoo
 */
public class ErrorBackToCallerCommand extends HystrixCommand<String> {
	protected ErrorBackToCallerCommand() {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("a")));
	}

	@Override
	protected String run() throws Exception {
		throw new IllegalArgumentException("illegal argument exception");
	}
}
