package com.xzchaoo.learn.test.demo20180412.service;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public class DbManager {
  public static final String REAL_CONSTANT_VALUE = "REAL_CONSTANT_VALUE";
  public static final String CLINIT_VALUE;
  public static final String CLINIT_METHOD_CALL = Integer.toString(1) + Integer.toString(2);

  static {
    CLINIT_VALUE = "CLINIT_VALUE";
    System.out.println("bye");
    System.exit(1);
  }

  public static String hello() {
    return "world";
  }

  public static void start() {

  }

  public static String hello2() {
    System.exit(1);
    return null;
  }
}
