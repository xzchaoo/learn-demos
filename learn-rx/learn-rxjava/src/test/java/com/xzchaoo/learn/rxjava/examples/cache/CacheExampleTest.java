package com.xzchaoo.learn.rxjava.examples.cache;

import org.junit.Test;

import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * @author xzchaoo
 * @date 2018/1/13
 */
public class CacheExampleTest {
	private Maybe<String> l1Cache(Object request) {
		return Maybe.defer(() -> {
			return Maybe.empty();
		});
	}

	private Maybe<String> l2Cache(Object request) {
		return Maybe.defer(() -> {
			return Maybe.empty();
		});
	}

	private Single<String> db(Object request) {
		return Single.defer(() -> {
			return Single.just(request.toString());
		});
	}

	@Test
	public void test() throws InterruptedException {
		Object request = "haha";
		Single<String> s = l1Cache(request)
			.switchIfEmpty(l2Cache(request))
			.switchIfEmpty(db(request));
		s.subscribe(result -> {
			System.out.println(result);
		}, error -> {
			error.printStackTrace();
		});
		System.out.println("sdfsdf");
		Thread.sleep(100);
	}
}
