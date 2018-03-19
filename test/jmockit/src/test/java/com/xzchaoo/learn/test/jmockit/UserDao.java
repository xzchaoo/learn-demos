package com.xzchaoo.learn.test.jmockit;

public class UserDao {
  public String find() {
    return "bar";
  }

  public void log() {

  }

  public static void static1() {
    System.out.println("bad static1");
  }
}
