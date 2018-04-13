package com.xzchaoo.learn.test.jmockit.demo20180413.service;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class UserDao {
  static {
    System.exit(1);
  }

  public Object findById(int id) {
    return "user" + id;
  }
}
