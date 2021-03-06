package com.xzchaoo.learn.ehcache3;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.event.EventType;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class Ehcache3Test {
	@Test
	public void test() throws Exception {
		//特点
		//强类型
		//大量采用builder模式

		CacheManager cm = CacheManagerBuilder.newCacheManagerBuilder()
			.withCache("a",
				CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, Long.class, ResourcePoolsBuilder.heap(10))
					.withExpiry(Expirations.timeToLiveExpiration(Duration.of(1, TimeUnit.SECONDS)))
					//.withExpiry(Expirations.noExpiration())
					.withValueCopier(new MyCopier())
					.withLoaderWriter(new MyCacheLoaderWriter())
					//添加监听器
					.add(CacheEventListenerConfigurationBuilder.newEventListenerConfiguration(event -> System.out.println(event), EventType.CREATED, EventType.UPDATED, EventType.EXPIRED).unordered().asynchronous())
			)
			.build(true);
		//cm.init();
		Cache<String, Long> ca = cm.getCache("a", String.class, Long.class);

		//运行时注册监听器
		//ca.getRuntimeConfiguration().registerCacheEventListener();

		assertNull(ca.get("a"));
		ca.put("a", 2L);
		ca.put("b", 3L);
		ca.remove("b");
		assertEquals(2, ca.get("a").longValue());
		Thread.sleep(900);
		assertEquals(2, ca.get("a").longValue());
		Thread.sleep(200);
		assertNull(ca.get("a"));
		cm.close();
	}
}
