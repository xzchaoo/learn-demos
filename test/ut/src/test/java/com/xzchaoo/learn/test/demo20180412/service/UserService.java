package com.xzchaoo.learn.test.demo20180412.service;

import com.xzchaoo.learn.test.demo20180412.model.User;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public interface UserService {
  User findById(int id);

  User createUser(String username);
}
