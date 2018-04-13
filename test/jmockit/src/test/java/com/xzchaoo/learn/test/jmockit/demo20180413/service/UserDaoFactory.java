package com.xzchaoo.learn.test.jmockit.demo20180413.service;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class UserDaoFactory {
  private static final UserDao USER_DAO = new UserDao();

  public static UserDao getUserDao() {
    return USER_DAO;
  }
}
