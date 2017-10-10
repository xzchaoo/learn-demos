package com.xzchaoo.learn.db.jdbc;

import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseJdbcTest {
	protected Connection c;

	@Before
	public void before() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		c = DriverManager.getConnection("jdbc:mysql://" + System.getProperty("ip57151") + ":3306/test?rewriteBatchedStatements=true", "root", "70862045");
		c.setAutoCommit(false);
		c.createStatement().execute("truncate table bi");
	}

	@After
	public void after() throws SQLException {
		c.close();
		c = null;
	}
}
