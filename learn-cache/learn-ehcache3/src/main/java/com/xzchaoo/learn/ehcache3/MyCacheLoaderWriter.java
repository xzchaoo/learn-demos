package com.xzchaoo.learn.ehcache3;

import org.ehcache.spi.loaderwriter.BulkCacheLoadingException;
import org.ehcache.spi.loaderwriter.BulkCacheWritingException;
import org.ehcache.spi.loaderwriter.CacheLoaderWriter;

import java.util.Map;

public class MyCacheLoaderWriter implements CacheLoaderWriter<String,Long> {
	@Override
	public Long load(String key) throws Exception {
		return null;
	}

	@Override
	public Map<String, Long> loadAll(Iterable<? extends String> keys) throws BulkCacheLoadingException, Exception {
		return null;
	}

	@Override
	public void write(String key, Long value) throws Exception {
		System.out.println(Thread.currentThread() + "write " + key + "=" + value);
	}

	@Override
	public void writeAll(Iterable<? extends Map.Entry<? extends String, ? extends Long>> entries) throws BulkCacheWritingException, Exception {
		for (Map.Entry<? extends String, ? extends Long> e : entries) {
			write(e.getKey(), e.getValue());
		}
	}

	@Override
	public void delete(String key) throws Exception {
		System.out.println("delete " + key);
	}

	@Override
	public void deleteAll(Iterable<? extends String> keys) throws BulkCacheWritingException, Exception {
		for (String key : keys) {
			delete(key);
		}
	}
}
