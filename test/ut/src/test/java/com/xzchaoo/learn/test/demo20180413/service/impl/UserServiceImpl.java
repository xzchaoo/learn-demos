package com.xzchaoo.learn.test.demo20180413.service.impl;


import com.xzchaoo.learn.test.demo20180413.dao.UserDao;
import com.xzchaoo.learn.test.demo20180413.model.User;
import com.xzchaoo.learn.test.demo20180413.service.UserService;

import javax.annotation.Resource;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class UserServiceImpl implements UserService {
  @Resource
  private UserDao userDao;

  @Resource
  private UserHelper userHelper;

  @Override
  public User findById(int id) {
    return userDao.findById(id);
  }

  @Override
  public void fillInfo(User user) {
    userHelper.fillExtraInfo1(user);
  }
}
