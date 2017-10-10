package com.xzchaoo.learn.db.jdbc;

import org.junit.Test;

/**
 * 批零更新有几种方式:
 * 1. 如果是要按某些条件进行统一的更新则可以: update ... set ... where ...
 * 2. 如果每个实体各有各的更新内容则, insert into ... values(...),() on duplicate key update ...
 * 3. TODO mysql似乎有replace语法 本质应该是 先删除再插入 破坏性比较大 可以试试
 * 4. TODO 临时表
 */
public class BatchUpdateJdbcTest extends BaseJdbcTest {
	@Test
	public void test1() {

	}
}
