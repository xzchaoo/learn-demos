package com.xzchaoo.learn.cache.caffeine;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 号称是一个高性能的缓存实现
 *
 * @author xzchaoo
 * @date 2018/1/3
 */
public class CaffeineTest {
	@Test
	public void test1() {
		Cache<String, String> cache = Caffeine.newBuilder()
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.maximumSize(10_000)
			.build();
// Lookup an entry, or null if not found
		String graph = cache.getIfPresent("a");
// Lookup and compute an entry if absent, or null if not computable
		graph = cache.get("a", k -> k + "X");
// Insert or update an entry
		cache.put("a", graph);
// Remove an entry
		cache.invalidate("a");
	}

	@Test
	public void test_sync_load() throws InterruptedException {
		//内存型
		LoadingCache<String, String> c = Caffeine.newBuilder()
			.maximumSize(10_000)
			.expireAfterWrite(5, TimeUnit.SECONDS)
			//.refreshAfterWrite(1, TimeUnit.SECONDS)
			.build(key -> {
				System.out.println("加载");
				return key + "X";
			});

		System.out.println(c.get("a"));
		Thread.sleep(2000);
		System.out.println(c.get("a"));
	}

	@Test
	public void test_async_load() {
		//内存型
		AsyncLoadingCache<String, String> c = Caffeine.newBuilder()
			.maximumSize(10_000)
			.expireAfterWrite(5, TimeUnit.MINUTES)
			.buildAsync((key, executor) -> {
				return CompletableFuture.completedFuture("");
			});

	}
}
