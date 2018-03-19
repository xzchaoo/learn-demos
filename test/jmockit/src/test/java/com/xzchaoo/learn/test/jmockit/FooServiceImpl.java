package com.xzchaoo.learn.test.jmockit;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author zcxu
 * @date 2018/3/19 0019
 */
public class FooServiceImpl implements FooService {
  private UserDao userDao;

  @Override
  public String callUserDao() {
    return userDao.find();
  }

  @Override
  public String callException() {
    return userDao.throwAnException();
  }

  @Override
  public String callPrivateMethod() {
    return null;
  }

  @Override
  public String callStaticMethod() {
    return FooUtils.hello("xzc");
  }

  @Override
  public void sendEmail(String message) {
    new FooEmailImpl().send(message);
  }

  @Override
  public int callHttp() {
    try {
      return new CallHttpTask().execute();
    } catch (IOException e) {
      return -1;
    }
  }

  //我们必须mock掉这个方法, 否则就抛异常了
  private String privateMethod() {
    throw new RuntimeException();
  }

  @PostConstruct
  public void init() {
    System.out.println("init");
  }

  @PreDestroy
  public void destroy() {
    System.out.println("destroy");
  }
}
