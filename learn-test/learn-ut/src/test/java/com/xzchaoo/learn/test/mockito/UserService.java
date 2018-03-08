package com.xzchaoo.learn.test.mockito;

/**
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class UserService {
  private UserDao userDao;

  public String foo() {
    return userDao.bar(1);
  }
}
