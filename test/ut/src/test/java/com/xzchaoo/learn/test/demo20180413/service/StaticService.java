package com.xzchaoo.learn.test.demo20180413.service;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class StaticService {
  public static String publicStaticMethod(int a, int b) {
    System.exit(1);
    return Integer.toString(a) + Integer.toString(b);
  }

  private static String privateStaticMethod(int a, int b) {
    System.exit(1);
    return Integer.toString(a) + Integer.toString(b);
  }

  public static String callPrivateStaticMethod(int a, int b) {
    return privateStaticMethod(a, b);
  }
}
