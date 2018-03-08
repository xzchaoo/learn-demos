package com.xzchaoo.learn.db.mybatis;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

public class MyBatisTest {
	@Test
	public void test1() {
		//code-based config
		Configuration cfg = new Configuration();
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(cfg);
		sqlSessionFactory.openSession();
	}
}
