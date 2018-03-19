package com.xzchaoo.learn.test.jmockit;

import java.io.IOException;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zcxu
 * @date 2018/3/16 0016
 */
public class FooEmailImpl implements FooEmail {
  @Getter
  @Setter
  private String title;

  @Override
  public void send(String message) {
    System.out.println(title + " send! " + message);
    //假设这里发生网络异常了 因此我们最好mock调这个方法
    throw new RuntimeException(new IOException());
  }

  public String otherMethod() {
    return "otherMethod";
  }
}
