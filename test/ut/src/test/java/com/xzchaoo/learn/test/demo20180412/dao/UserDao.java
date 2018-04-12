package com.xzchaoo.learn.test.demo20180412.dao;

import com.xzchaoo.learn.test.demo20180412.model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public class UserDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);

  private final DataSource datasource;

  public UserDao(DataSource datasource) {
    this.datasource = datasource;
  }

  public User findById(int id) {
    try {
      Connection conn = datasource.getConnection();
      // execute sql
      conn.close();
      System.exit(1);
      return null;
    } catch (SQLException e) {
      LOGGER.error("SQL ERROR", e);
      return null;
    }
  }
}
