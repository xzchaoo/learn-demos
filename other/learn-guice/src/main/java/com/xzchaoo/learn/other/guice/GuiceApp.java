package com.xzchaoo.learn.other.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.junit.Test;

public class GuiceApp {
	@Test
	public void test() {
		Injector injector = Guice.createInjector(new ServiceModule());
		UserService us = injector.getInstance(UserService.class);
		UserDao ud = us.getUserDao();
		System.out.println(ud);
		System.out.println(((UserDaoImpl) ud).getXxxSql());
	}
}
