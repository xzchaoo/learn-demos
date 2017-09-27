package com.xzchaoo.learn.db.h2;

import org.apache.commons.io.FileUtils;
import org.h2.tools.SimpleResultSet;
import org.junit.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

import static org.junit.Assert.*;

public class H2Test {
	@Test
	public void test_SimpleResultSet() throws Exception {
		SimpleResultSet rs = new SimpleResultSet();
		rs.addColumn("ID", Types.INTEGER, 10, 0);
		rs.addColumn("NAME", Types.VARCHAR, 255, 0);
		rs.addRow(0, "Hello");
		rs.addRow(1, "World");
		assertTrue(rs.next());
		assertEquals(0, rs.getInt(1));
		assertEquals("Hello", rs.getString(2));
		assertTrue(rs.next());
		assertEquals(1, rs.getInt(1));
		assertEquals("World", rs.getString(2));
		assertFalse(rs.next());
	}

	@Test
	public void test_init() throws Exception {
		Class.forName("org.h2.Driver");
		Connection c = DriverManager.getConnection("jdbc:h2:mem:");
		File file = new File(getClass().getClassLoader().getResource("init.sql").toURI());
		//RunScript.execute(c, new FileReader(url));
		String sql = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
		//JdbcUtils.
		//Script.process();
		try ( Statement s = c.createStatement() ) {
			System.out.println(s.execute(sql));
		}
		try ( Statement s = c.createStatement() ) {
			ResultSet rs = s.executeQuery("select id,username from users");
			while(rs.next()){
				System.out.println(rs.getInt(1));
				System.out.println(rs.getString(2));
			}
		}
	}

	@Test
	public void test1() throws Exception {
		Class.forName("org.h2.Driver");
		Connection c = DriverManager.getConnection("jdbc:h2:./test.mv.db;MODE=MSSQLServer");
		PreparedStatement ps = c.prepareStatement("select * from users with (nolock)");
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getInt(1));
			System.out.println(rs.getString(2));
		}
		rs.close();
		ps.close();
		c.close();
	}
}