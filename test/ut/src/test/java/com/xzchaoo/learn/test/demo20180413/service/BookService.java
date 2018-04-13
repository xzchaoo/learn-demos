package com.xzchaoo.learn.test.demo20180413.service;

import com.xzchaoo.learn.test.demo20180413.dao.BookDao;

import org.apache.commons.lang3.StringUtils;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class BookService {
  public static final String STATIC_1 = "1";
  public static final String STATIC_2 = Integer.toString(2);
  public static final String STATIC_3;

  static {
    STATIC_3 = "3";
    System.exit(1);
  }

  public String getString(String x) {
    return BookDao.getInstance().toString() + x;
  }

  public boolean isEmpty(String str) {
    return StringUtils.isEmpty(str);
  }

  public int add2(int x) {
    return getValue2() + x;
  }

  private int getValue2() {
    System.exit(1);
    return 2;
  }

  private static int add3(int x) {
    System.exit(1);
    return 3 + x;
  }
}
