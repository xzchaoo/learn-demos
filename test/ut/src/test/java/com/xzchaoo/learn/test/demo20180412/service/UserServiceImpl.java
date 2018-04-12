package com.xzchaoo.learn.test.demo20180412.service;

import com.xzchaoo.learn.test.demo20180412.dao.UserDao;
import com.xzchaoo.learn.test.demo20180412.dao.UserDaoFactory;
import com.xzchaoo.learn.test.demo20180412.model.User;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public class UserServiceImpl implements UserService {
  private static final UserDao USER_DAO = UserDaoFactory.getInstance();

  @Override
  public User findById(int id) {
    return USER_DAO.findById(id);
  }

  @Override
  public User createUser(String username) {
    return null;
  }
}
