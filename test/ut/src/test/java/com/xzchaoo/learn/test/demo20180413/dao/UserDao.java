package com.xzchaoo.learn.test.demo20180413.dao;

import com.xzchaoo.learn.test.demo20180413.model.User;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public interface UserDao {
  User findById(int id);
}
