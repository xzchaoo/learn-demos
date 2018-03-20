package com.xzchaoo.learn.test.jmockit.interfacetest;

/**
 * @author zcxu
 * @date 2018/3/20 0020
 */
public class Service1Impl implements Service1 {
  private Dao1 dao1;

  @Override
  public String f1() {
    return dao1.f1();
  }
}
