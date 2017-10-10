package com.xzchaoo.learn.db.jdbc;

import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 批零更新有几种方式:
 * 1. 如果是要按某些条件进行统一的更新则可以: update ... set ... where ...
 * 2. 如果每个实体各有各的更新内容则, insert into ... values(...),() on duplicate key update ...
 * 3. TODO mysql似乎有replace语法 本质应该是 先删除再插入 破坏性比较大 可以试试
 * 4. TODO 临时表
 */
public class BatchUpdateJdbcTest extends BaseJdbcTest {
	private int count = 100;

	@Test
	public void test_single() throws SQLException {
		long begin = System.currentTimeMillis();
		PreparedStatement ps = c.prepareStatement("update bi set k1 = ? where id = ?");
		for (int i = 0; i < count; ++i) {
			ps.setString(1, "b" + i);
			ps.setInt(2, i + 1);
			ps.execute();
		}
		c.commit();
		long end = System.currentTimeMillis();
		System.out.println("更新" + count + "条数据 耗时=" + (end - begin) + "毫秒");
	}

	@Test
	public void test_batch() throws SQLException {
		long begin = System.currentTimeMillis();
		PreparedStatement ps = c.prepareStatement("update bi set k1 = ? where id = ?");
		for (int i = 0; i < count; ++i) {
			ps.setString(1, "b" + i);
			ps.setInt(2, i + 1);
			ps.addBatch();
		}
		ps.executeBatch();
		c.commit();
		long end = System.currentTimeMillis();
		System.out.println("更新" + count + "条数据 耗时=" + (end - begin) + "毫秒");
	}
}
