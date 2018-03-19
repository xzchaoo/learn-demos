package com.xzchaoo.learn.test.jmockit;

/**
 * @author zcxu
 * @date 2018/3/16 0016
 */
public class BarDao {
  static{
    System.out.println("BarDao Class init");
  }
  public BarDao() {
    System.out.println("BarDao init");
  }

  public void badSave() {
    System.out.println("bye bye");
    System.exit(0);
  }
}
