/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用这个类来模拟很多的第三方类 很多第三方类会在静态构造函数里做一些初始化的方法 这可能是我们不想要的 因此要把它们镇压掉
 */
public class BadStaticInitClass {
	private static final Logger LOGGER = LoggerFactory.getLogger(BadStaticInitClass.class);

	static {
		System.out.println("这个类有静态的构造方法 你必须镇压它 1");
	}

	public static String foo() {
		LOGGER.info("foo");
		return "bar";
	}
}
