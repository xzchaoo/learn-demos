package com.xzchaoo.learn.test.demo20180413.service;

import com.xzchaoo.learn.test.demo20180413.model.User;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public interface UserService {
  User findById(int id);

  void fillInfo(User user);
}
