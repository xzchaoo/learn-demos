package com.xzchaoo.learn.test.demo20180413.dao.impl;

import com.xzchaoo.learn.test.demo20180413.dao.UserDao;
import com.xzchaoo.learn.test.demo20180413.model.User;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class UserDaoImpl implements UserDao {
  @Override
  public User findById(int id) {
    if (id > 10) {
      return null;
    }
    User user = new User();
    user.setId(id);
    user.setUsername("user" + id);
    return user;
  }
}
