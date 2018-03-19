package com.xzchaoo.learn.test.powermock.mocknewinstance;

import com.xzchaoo.learn.test.UserDaoImpl;
import com.xzchaoo.learn.test.powermock.ignore.FooUtils;

import java.io.UnsupportedEncodingException;

/**
 * @author zcxu
 * @date 2018/3/19 0019
 */
public class FooService {
  public void foo() {
    UserDaoImpl u = new UserDaoImpl();
    System.out.println(u.findUserByUserId(1));
    u.f1(1);
    try {
      System.out.println(FooUtils.encode("A", "a"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
  }
}
