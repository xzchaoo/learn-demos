package com.xzchaoo.learn.jdk8;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author xzchaoo
 * @date 2018/1/3
 */
public class FutureTest {
	@Test
	public void test() {
		CompletableFuture<String> cf1 = new CompletableFuture<>();
		CompletableFuture<Integer> cf2 = cf1.thenApply(new Function<String, Integer>() {
			@Override
			public Integer apply(String s) {
				return Integer.parseInt(s);
			}
		});
	}
}
