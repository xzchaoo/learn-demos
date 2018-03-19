package com.xzchaoo.learn.test.jmockit;

public class UserDao {
  static {
    System.out.println("UserDao 静态构造器");
  }

  public UserDao() {
    System.out.println("UserDao 构造器");
  }

  public String find() {
    return "bar";
  }

  public void log() {

  }

  public static final void static1() {
    System.out.println("bad static1");
  }

  /**
   * mock这个方法, 使得它抛出一个异常
   *
   * @return
   */
  public String throwAnException() {
    return "throwAnException";
  }
}
