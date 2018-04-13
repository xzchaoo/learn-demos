package com.xzchaoo.learn.test.jmockit.demo20180413.service;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class StaticService {
  public static int publicStaticMethod(int a, int b) {
    System.exit(1);
    return a + b;
  }

  private static int privateStaticMethod(int a, int b) {
    System.exit(1);
    return a + b;
  }

  public static int callPrivateStaticMethod(int a, int b) {
    return privateStaticMethod(a, b);
  }
}
