package com.xzchaoo.learn.test.jmockit;

/**
 * @author zcxu
 * @date 2018/3/19 0019
 */
public class FooUtils {
  public static final String STATIC_1 = "static1";
  public static final String STATIC_2;
  public static final String STATIC_3 = generate("static3");

  public static String generate(String str) {
    return str;
  }

  static {
    //必须阻止构造函数的执行 否则会影响你的测试
    STATIC_2 = "static2";
    System.exit(-1);
  }

  private FooUtils() {
  }

  public static String returnHello() {
    return "hello";
  }

  public static String hello(String name) {
    return "hello " + name;
  }
}
