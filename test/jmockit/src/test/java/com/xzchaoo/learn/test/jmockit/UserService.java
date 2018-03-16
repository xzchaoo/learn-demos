package com.xzchaoo.learn.test.jmockit;

import lombok.Getter;
import lombok.Setter;


public class UserService {
  @Getter
  @Setter
  private UserDao userDao;
  public String foo(){
    return userDao.find();
  }
}
