package com.xzchaoo.learn.test.jmockit;

/**
 * @author zcxu
 * @date 2018/3/19 0019
 */
public interface FooService {
  String callUserDao();

  String callException();

  String callPrivateMethod();

  String callStaticMethod();

  void sendEmail(String message);

  int callHttp();
}
