package com.xzchaoo.learn.db.jdbc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 批量插入的方式
 * 1. 在一个事务里, 发送多次单条语句
 * 2. 在一个事务里, 发送单次多条语句 [不知道如何实现]
 * 3. 在一个事务里, 利用 insert into ... values(...),(...) 语法
 * 1. 可以有变种, 比如要批量插入1W个元素, 拆成每100个一批可能会比1W个一批效果好
 * 4. 在一个事务里, 利用JDBC的batch语法 [不知道这种语法的本质实现是什么]
 * 5. 最后应该考虑多事务的情况, 在1个事务里批量插入1W个记录, 其实server端为了支持回滚, 需要缓存足够多的数据
 * 运行这个例子需要实际数据库的支持(为了放大网络耗时) 因此放到 main 目录下
 */
public class BatchInsertJdbcTest {

	private Connection c;
	private int count = 1000;

	@Before
	public void before() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		c = DriverManager.getConnection("jdbc:mysql://" + System.getProperty("ip57151") + ":3306/test", "root", "70862045");
		c.setAutoCommit(false);
		c.createStatement().execute("truncate table bi");
	}

	@After
	public void after() throws SQLException {
		c.close();
		c = null;
	}

	@Test
	public void test_single() throws SQLException {
		PreparedStatement ps = c.prepareStatement("insert into bi(k1) values(?)", Statement.RETURN_GENERATED_KEYS);
		long begin = System.currentTimeMillis();
		for (int i = 0; i < count; ++i) {
			ps.setString(1, "a" + i);
			ps.execute();
		}
		c.commit();
		long end = System.currentTimeMillis();
		System.out.println("插入了" + count + "条数据 耗时=" + (end - begin) + "毫秒");
		ps.close();
	}

	/**
	 * 效率最高
	 *
	 * @throws SQLException
	 */
	@Test
	public void test_insertBatch() throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into bi(k1) values");
		for (int i = 0; i < count; ++i) {
			sb.append("(?),");
		}
		sb.deleteCharAt(sb.length() - 1);
		System.out.println(sb.toString());

		long begin = System.currentTimeMillis();
		PreparedStatement ps = c.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
		for (int i = 0; i < count; ++i) {
			ps.setString(i + 1, "a" + i);
		}
		ps.execute();
		//通过下面的方式可以获得生成的key
//		ResultSet rs = ps.getGeneratedKeys();
//		while (rs.next()) {
//			System.out.println(rs.getInt(1));
//		}
		c.commit();
		long end = System.currentTimeMillis();
		System.out.println("插入了" + count + "条数据 耗时=" + (end - begin) + "毫秒");
		ps.close();
	}

	/**
	 * 本质上还是 发出多次请求
	 *
	 * @throws SQLException
	 */
	@Test
	public void test_jdbcBatch() throws SQLException {
		long begin = System.currentTimeMillis();
		PreparedStatement ps = c.prepareStatement("insert into bi(k1) values(?)", Statement.RETURN_GENERATED_KEYS);
		for (int i = 0; i < count; ++i) {
			ps.setString(1, "a" + i);
			ps.addBatch();
		}
		//这里的results是响应码 执行成功则为1
		int[] results = ps.executeBatch();
//		for (int r : results) {
//			System.out.println(r);
//		}
		//通过下面的方式可以拿到keys
//		ResultSet rs = ps.getGeneratedKeys();
//		while (rs.next()) {
//			System.out.println(rs.getInt(1));
//		}
		c.commit();
		long end = System.currentTimeMillis();
		System.out.println("插入了" + count + "条数据 耗时=" + (end - begin) + "毫秒");
		ps.close();
	}
}
