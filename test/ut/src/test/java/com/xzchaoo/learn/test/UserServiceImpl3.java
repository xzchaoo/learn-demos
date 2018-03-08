package com.xzchaoo.learn.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImpl3 {
	@Mock
	private UserDao userDao;

	@Test
	public void ceshi() {
		System.out.println(userDao);
	}
}
