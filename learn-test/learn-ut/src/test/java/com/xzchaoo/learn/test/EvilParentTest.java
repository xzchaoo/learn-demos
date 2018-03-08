/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.test;

import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class EvilParentTest extends TestBase {
	@Test
	public void test() {
		//使用这个方法可以绕过构造函数 但是就不能使用 @InjectMocks 了
		EvilParent e = Whitebox.newInstance(EvilParent.class);
	}
}
