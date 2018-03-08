package com.xzchaoo.learn.jdk8.servicelocator;

import org.junit.Test;

import java.util.ServiceLoader;

/**
 * @author xzchaoo
 * @date 2017/12/30
 */
public class ServiceLocatorTest {
	@Test
	public void test() {
		ServiceLoader<ServiceLocatorTestInterface1> ss = ServiceLoader.load(ServiceLocatorTestInterface1.class);
		ss.iterator().forEachRemaining(s -> System.out.println(s.foo()));
	}
}
