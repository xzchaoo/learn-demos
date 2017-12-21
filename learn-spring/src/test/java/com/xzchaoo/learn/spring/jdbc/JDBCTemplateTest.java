package com.xzchaoo.learn.spring.jdbc;

import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JDBCTemplateTest {
	@Test
	public void test1() {
		EmbeddedDatabase ed = new EmbeddedDatabaseBuilder()
			.setType(EmbeddedDatabaseType.H2)
			.setName("test")
			.generateUniqueName(true)
			.build();
		System.out.println(ed);
		JdbcTemplate jt = new JdbcTemplate(ed);
		jt.execute("create table users(id integer primary key, username varchar(50) not null)");
		jt.execute("insert into users(id, username) values(?,?)", (PreparedStatementCallback<Void>) ps -> {
			ps.setInt(1, 1);
			ps.setString(2, "xzc");
			ps.execute();
			return null;
		});
		List<String> list = jt.query("select id,username from users", new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1) + " " + rs.getString(2);
			}
		});
		System.out.println(list);
		ed.shutdown();
	}
}
