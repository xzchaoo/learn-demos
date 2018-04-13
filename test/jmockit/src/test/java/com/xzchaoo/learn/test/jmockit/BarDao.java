package com.xzchaoo.learn.test.jmockit;

/**
 * @author zcxu
 * @date 2018/3/16 0016
 */
public class BarDao {
  static {
     System.exit(1);
  }

  public BarDao() {
     System.exit(1);
  }

  public void badSave() {
     System.exit(1);
  }
}
