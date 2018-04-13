package com.xzchaoo.learn.test.demo20180413.service;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class FinalService {
  public final String publicFinalMethod() {
    System.exit(1);
    return null;
  }

  private final String finalMethod() {
    System.exit(1);
    return null;
  }

  public String callFinalMethod() {
    return finalMethod();
  }
}
