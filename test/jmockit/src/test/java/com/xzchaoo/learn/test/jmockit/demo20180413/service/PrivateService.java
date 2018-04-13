package com.xzchaoo.learn.test.jmockit.demo20180413.service;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class PrivateService {
  private int privateMethod(int a, int b) {
    System.exit(1);
    return a + b;
  }

  public int callPrivateMethod(int a, int b) {
    return privateMethod(a, b);
  }
}
