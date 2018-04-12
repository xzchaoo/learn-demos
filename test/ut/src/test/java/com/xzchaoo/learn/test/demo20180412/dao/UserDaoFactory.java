package com.xzchaoo.learn.test.demo20180412.dao;

import com.xzchaoo.learn.test.demo20180412.dao.UserDao;

import javax.sql.DataSource;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public class UserDaoFactory {
  private static final UserDao USER_DAO;

  static {
    // 连接数据库, 假设在无网环境也行成功 那么必须镇压该静态构造函数
    System.exit(1);
    DataSource ds = null;
    USER_DAO = new UserDao(ds);
  }


  public static UserDao getInstance() {
    return USER_DAO;
  }
}
