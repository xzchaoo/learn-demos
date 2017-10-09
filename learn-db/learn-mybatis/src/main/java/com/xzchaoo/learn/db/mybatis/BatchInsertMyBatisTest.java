package com.xzchaoo.learn.db.mybatis;

import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 这里就不测试 insert into ... values(...), (...) 的语法了 它肯定也是最快的-
 */
public class BatchInsertMyBatisTest {

	private Connection c;
	private int count = 100;
	private SqlSessionFactory ssf;

	@Before
	public void before() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		c = DriverManager.getConnection("jdbc:mysql://" + System.getProperty("ip57151") + ":3306/test", "root", "70862045");
		c.setAutoCommit(false);
		c.createStatement().execute("truncate table bi");
		Configuration cfg = new Configuration();
		cfg.addMapper(BIMapper.class);
		UnpooledDataSource uds = new UnpooledDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://" + System.getProperty("ip57151") + ":3306/test", "root", "70862045");
		cfg.setEnvironment(new Environment("default", new JdbcTransactionFactory(), uds));
		ssf = new SqlSessionFactoryBuilder().build(cfg);
	}

	@After
	public void after() throws SQLException {
		c.close();
		c = null;
	}

	@Test
	public void test_single() {
		SqlSession ss = ssf.openSession(ExecutorType.SIMPLE, false);
		BIMapper bm = ss.getMapper(BIMapper.class);
		long begin = System.currentTimeMillis();
		for (int i = 0; i < count; ++i) {
			bm.insert("a" + i);
		}
		ss.commit();
		long end = System.currentTimeMillis();
		System.out.println("插入了" + count + "条数据 耗时=" + (end - begin) + "毫秒");
		System.out.println(bm.count());
	}

	@Test
	public void test_batch() {
		SqlSession ss = ssf.openSession(ExecutorType.BATCH, false);
		BIMapper bm = ss.getMapper(BIMapper.class);
		long begin = System.currentTimeMillis();
		for (int i = 0; i < count; ++i) {
			bm.insert("a" + i);
		}
		ss.commit();
		long end = System.currentTimeMillis();
		System.out.println("插入了" + count + "条数据 耗时=" + (end - begin) + "毫秒");
		System.out.println(bm.count());
	}

	@Test
	public void test_reuse() {
		SqlSession ss = ssf.openSession(ExecutorType.REUSE, false);
		BIMapper bm = ss.getMapper(BIMapper.class);
		long begin = System.currentTimeMillis();
		for (int i = 0; i < count; ++i) {
			bm.insert("a" + i);
		}
		ss.commit();
		long end = System.currentTimeMillis();
		System.out.println("插入了" + count + "条数据 耗时=" + (end - begin) + "毫秒");
		System.out.println(bm.count());
	}

}
