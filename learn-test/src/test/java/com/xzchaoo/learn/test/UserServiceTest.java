/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserServiceTest extends TestBase {
	@Before
	public void before() {
		//MockitoAnnotations.initMocks(this);
		System.out.println("before");
	}

	@BeforeClass
	public static void beforeClass() {
		System.out.println("beforeClass");
	}

	@After
	public void after() {
		System.out.println("after");
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("afterClass");
	}

	@Ignore
	@Test
	public void test_getUsernameByUserId_1() {
		UserServiceImpl us = new UserServiceImpl(new UserDaoImpl());
		assertEquals("xzc 1", us.getUsernameByUserId(1));
	}

	@Mock
	private UserDao ud;

	@InjectMocks
	UserServiceImpl us;

	@Test(timeout = 1000)
	public void test_getUsernameByUserId_2() {
		//UserDao ud = mock(UserDao.class);
		when(ud.findUserByUserId(2)).thenAnswer(new Answer<User>() {
			public User answer(InvocationOnMock invocation) throws Throwable {
				User user = new User();
				user.setId(2);
				user.setName("xzc 2");
				return user;
			}
		});

		assertEquals("xzc 2", us.getUsernameByUserId(2));
		ArgumentCaptor<Integer> argC = ArgumentCaptor.forClass(int.class);
		verify(ud, timeout(1000).times(1)).findUserByUserId(argC.capture());
		System.out.println(argC.getValue());

		//reset(); reset掉mock
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_getUsernameByUserId_3() {
		throw new IllegalArgumentException();
	}
}
