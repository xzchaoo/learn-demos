package com.xzchaoo.learn.other.guice;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserService {
	@Inject
	private UserDao userDao;

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
}
