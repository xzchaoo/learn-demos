package com.xzchaoo.learn.demos.db.sqlite;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * created by xzchaoo at 2017/11/21
 *
 * @author xzchaoo
 */
public class SqliteTest {
	@Test
	public void test() throws IOException {
		//jdbc:sqlite::memory:
		File file = File.createTempFile("sqlite", ".db");
		DriverManagerDataSource ds = new DriverManagerDataSource(
			"jdbc:sqlite:" + file.toURI()
		);
		file.deleteOnExit();
		ds.setDriverClassName("org.sqlite.JDBC");
		JdbcTemplate jt = new JdbcTemplate(ds);
		jt.execute("create table users(id integer auto_increment, username text)");
		jt.update("insert into users(username) values(?)", new Object[]{"a"});
		List<String> list = jt.query("select * from users", new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(2);
			}
		});
		System.out.println(list);
	}
}
