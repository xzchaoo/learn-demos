package com.xzchaoo.learn.test.jmockit;

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
  }
}
