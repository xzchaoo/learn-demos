package com.xzchaoo.learn.test.mockito;

/**
 * @author xzchaoo
 * @date 2018/4/9
 */
public class PrivateObject {
  public void foo() {
    System.out.println("foo");
    bar("haha");
  }

  private void bar(String str) {
    System.out.println(str);
    throw new RuntimeException();
  }
}
