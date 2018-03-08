package com.xzchaoo.learn.spring.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * created by xzchaoo at 2017/12/13
 *
 * @author xzchaoo
 */
@Configuration
@EnableCaching
@ComponentScan
public class SpringCacheApp {
	@Bean
	public CacheManager cacheManager() {
		EhCacheCacheManager cm = new EhCacheCacheManager();
		return cm;
	}
}
