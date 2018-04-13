package com.xzchaoo.learn.test.jmockit.demo20180413.service;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class UserService {
  private static final UserDao USER_DAO = UserDaoFactory.getUserDao();

  public Object findById(int id) {
    return USER_DAO.findById(id);
  }
}
