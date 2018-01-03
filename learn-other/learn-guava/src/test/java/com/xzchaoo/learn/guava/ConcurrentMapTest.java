package com.xzchaoo.learn.guava;

import com.google.common.collect.MapMaker;

import org.junit.Test;

import java.util.concurrent.ConcurrentMap;

/**
 * @author xzchaoo
 * @date 2017/12/30
 */
public class ConcurrentMapTest {
	@Test
	public void test() {
		ConcurrentMap<String, Object> cm = new MapMaker()
			.weakValues()
			.makeMap();
		cm.put("a", new Object());
		System.out.println(cm.get("a"));
		System.gc();
		System.out.println(cm.get("a"));
	}
}
