package com.xzchaoo.learn.jdk8.classtest;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodTest {
  @Test
  public void test() throws NoSuchMethodException {
    assertThat("".getClass().getMethod("wait")).isNotNull();
  }

  @Test(expected = NoSuchMethodException.class)
  public void test2() throws NoSuchMethodException {
    ((Object) "").getClass().getDeclaredMethod("wait");
  }
}
