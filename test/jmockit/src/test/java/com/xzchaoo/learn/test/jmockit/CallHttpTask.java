package com.xzchaoo.learn.test.jmockit;

import java.io.IOException;

/**
 * @author zcxu
 * @date 2018/3/19 0019
 */
public class CallHttpTask {
  public int execute() throws IOException {
    throw new IOException();
  }

  public final String returnAbc() {
    return "abc";
  }
}
