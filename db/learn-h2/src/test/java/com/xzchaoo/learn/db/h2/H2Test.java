package com.xzchaoo.learn.db.h2;

import org.apache.commons.io.FileUtils;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.tools.SimpleResultSet;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 单元测试方法
 * 方案1: 将数据整理成一个db文件, 放到classpath下, UT开始的时候将其复制到临时目录, 然后标记文件为deleteOnExit, 然后就可以用文件系统的方式打开该数据库了
 * 方案2: 所有数据dump成SQL文件, 初始化的时候执行脚本
 */
public class H2Test {

  @Test
  public void test_simple1() throws Exception {
    //默认情况下 内存数据库的所有连接关闭之后 它的内容也清除了, 这会导致下次打开的时候没有上次的修改
    //如果不给内存数据库起一个名字, 比如下面的test, 那么每次打开连接连的都是一个私有的数据库
    //String url = "jdbc:h2:mem:test;MODE=MYSQL;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:init.sql'\\;";
    //runscript 后面不知道为什么一定要 \\; 必须被转义
    //MYSQL兼容模式 但不一定支持MYSQL的所有语法
    //TRACE_LEVEL_FILE=3;TRACE_LEVEL_SYSTEM_OUT=3 用于打印更多日志
    //0OFF 1ERROR 2INFO 3DEBUG
    //TRACE_LEVEL_FILE=4 表示使用SLF4J
    String url = "jdbc:h2:mem:test;MODE=MYSQL;INIT=RUNSCRIPT FROM 'classpath:init.sql';IGNORE_UNKNOWN_SETTINGS=TRUE;TRACE_LEVEL_FILE=3;TRACE_LEVEL_SYSTEM_OUT=3;";
    Class.forName("org.h2.Driver");
    Connection conn = DriverManager.getConnection(url, "sa", "");
    conn.close();
  }

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
    JdbcConnectionPool ds = JdbcConnectionPool.create("jdbc:h2:mem:;MODE=MSSQLServer", "", "");
    Connection c = ds.getConnection();
    //c.setAutoCommit(false);
    File file = new File(getClass().getClassLoader().getResource("init.sql").toURI());
    //RunScript.execute(c, new FileReader(url));
    String sql = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    //JdbcUtils.
    //Script.process();
    try (Statement s = c.createStatement()) {
      System.out.println(s.execute(sql));
    }
    try (Statement s = c.createStatement()) {
      ResultSet rs = s.executeQuery("select id,username from users");
      while (rs.next()) {
        System.out.println(rs.getInt(1));
        System.out.println(rs.getString(2));
      }
    }
    c.commit();
    c.close();

    JdbcTemplate jt = new JdbcTemplate(ds);
    List<Object[]> list = jt.query("select id,username from users", new RowMapper<Object[]>() {
      @Override
      public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Object[]{rs.getInt(1), rs.getString(2)};
      }
    });
    System.out.println(list.size());
  }

  @Test
  public void test1() throws Exception {
    //TRACE_LEVEL_SYSTEM_OUT=3 0off 1error 2info 3debug 4=slf4j
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
