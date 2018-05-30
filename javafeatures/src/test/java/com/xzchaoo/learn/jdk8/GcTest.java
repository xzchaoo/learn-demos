package com.xzchaoo.learn.jdk8;

import org.junit.Test;

import java.util.Arrays;

/**
 * @author xzchaoo
 * @date 2018/5/28
 */
public class GcTest {
  private static final int _1MB = 1024 * 1024;

  @Test
  public void test() {
    byte[] b = new byte[10 * _1MB];
    System.out.println(Arrays.toString(b));
  }
}
