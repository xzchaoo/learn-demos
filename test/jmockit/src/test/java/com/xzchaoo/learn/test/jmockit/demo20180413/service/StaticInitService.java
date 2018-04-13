package com.xzchaoo.learn.test.jmockit.demo20180413.service;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class StaticInitService {
  static {
    System.exit(1);
  }

  public String getValue() {
    return "success";
  }
}
