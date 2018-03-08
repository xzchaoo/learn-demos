/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.test;

import org.junit.Test;

import static org.junit.Assert.*;

public class BadStaticInitClassTest extends TestBase {
	@Test(expected = NullPointerException.class)
	public void test_foo() {
		//镇压了静态构造函数 但是会导致 静态变量无法赋值...
		String foo = BadStaticInitClass.foo();
		assertEquals("bar", foo);
	}
}
