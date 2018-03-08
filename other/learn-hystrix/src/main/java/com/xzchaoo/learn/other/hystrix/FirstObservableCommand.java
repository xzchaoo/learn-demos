package com.xzchaoo.learn.other.hystrix;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixObservableCommand;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * created by xzchaoo at 2017/10/25
 *
 * @author xzchaoo
 */
public class FirstObservableCommand extends HystrixObservableCommand<String> {
	protected FirstObservableCommand() {
		super(
			Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("FOG"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("FOC"))
		);
	}

	@Override
	protected Observable<String> construct() {
		return Observable.timer(2, TimeUnit.SECONDS).map(x -> "x" + x);
	}
}
