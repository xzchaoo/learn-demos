package com.xzchaoo.learn.ehcache3;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class Ehcache3Test {
	@Test
	public void test() {
		//强类型

		//大量采用builder模式

		CacheManager cm = CacheManagerBuilder.newCacheManagerBuilder()
			.withCache("a",
				CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Long.class, ResourcePoolsBuilder.heap(10))
					.withExpiry(Expirations.timeToLiveExpiration(Duration.of(1, TimeUnit.MINUTES)))
			)
			.build(true);
		//cm.init();
		Cache<String, Long> ca = cm.getCache("a", String.class, Long.class);
		System.out.println(ca.get("a"));
		ca.put("a", 2L);
		System.out.println(ca.get("a"));
		cm.close();
	}
}
