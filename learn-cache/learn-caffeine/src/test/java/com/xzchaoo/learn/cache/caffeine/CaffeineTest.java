package com.xzchaoo.learn.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import org.junit.Test;

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
	public void test2() {
		//内存型
		LoadingCache<String, String> graphs = Caffeine.newBuilder()
			.maximumSize(10_000)
			.expireAfterWrite(5, TimeUnit.MINUTES)
			.refreshAfterWrite(1, TimeUnit.MINUTES)
			.build(key -> key + "X");
	}
}
