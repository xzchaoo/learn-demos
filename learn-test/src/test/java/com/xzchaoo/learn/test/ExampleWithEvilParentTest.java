/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.test;

import org.junit.Test;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.support.membermodification.MemberMatcher.constructor;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

public class ExampleWithEvilParentTest extends TestBase {
	@Mock
	private UserService userService;

	@Test
	public void test_getUsernameByUserId() {
		suppress(constructor(EvilParent.class));
		ExampleWithEvilParent e = Whitebox.newInstance(ExampleWithEvilParent.class);
		Whitebox.setInternalState(e, userService);
		when(userService.getUsernameByUserId(1)).then(i -> {
			return "xzc";
		});
		assertEquals("xzc", e.getUsernameByUserId(1));
	}
}
