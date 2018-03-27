package com.xzchaoo.learn.jdk8.jvm;

import org.junit.Test;

/**
 * TODO 触发类加载的几个操作
 *
 * @author zcxu
 * @date 2018/3/26 0026
 */
public class ClassInitTest {
  @Test
  public void test() {
    Class1[] cs = new Class1[2];
    System.out.println((new Object() instanceof Class1));
    System.out.println(new Class1());
  }
}
