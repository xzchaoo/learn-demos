package com.xzchaoo.learn.jdk8.servicelocator;

/**
 * @author xzchaoo
 * @date 2017/12/30
 */
public class ServiceLocatorTestInterface1Impl implements ServiceLocatorTestInterface1 {
	@Override
	public String foo() {
		return "f1;";
	}
}
