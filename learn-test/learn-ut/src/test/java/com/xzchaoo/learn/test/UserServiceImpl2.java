package com.xzchaoo.learn.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class UserServiceImpl2 {
	@Mock
	private UserDao userDao;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void ceshi() {
		System.out.println(userDao);
	}
}
