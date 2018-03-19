package com.xzchaoo.learn.test.powermock.preparetest;

import com.xzchaoo.learn.test.UserDaoImpl;
import com.xzchaoo.learn.test.powermock.ignore.FooUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author zcxu
 * @date 2018/3/19 0019
 */
public class PrepareService {
  public String callStaticFinalMethod() {
    try {
      return FooUtils.encode("a", "b");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public String callNewInstance() {
    return new UserDaoImpl().findUserByUserId(1).getName();
  }

  public String callFinalMethod() {
    return userDao.f2();
  }

  private UserDaoImpl userDao;

  public String callPrivateMethod() {
    return privateMethod();
  }

  private String privateMethod() {
    return "privateMethod";
  }
}
