package com.xzchaoo.learn.other.guice;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserDaoImpl implements UserDao{
	@Inject
	private XxxSql xxxSql;

	//不会执行
	@PostConstruct
	public void init() {
		System.out.println("dao init");
	}

	public XxxSql getXxxSql() {
		return xxxSql;
	}

	public void setXxxSql(XxxSql xxxSql) {
		this.xxxSql = xxxSql;
	}
}
