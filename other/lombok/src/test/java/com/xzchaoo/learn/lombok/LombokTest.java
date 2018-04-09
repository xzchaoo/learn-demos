package com.xzchaoo.learn.lombok;

import org.junit.Test;

import lombok.experimental.var;
import lombok.val;

/**
 * @author zcxu
 * @date 2018/4/8 0008
 */
public class LombokTest {
  @Test
  public void test() {
    val fe = new FooEntity();
    fe.setId(1);
    fe.setUsername("foo");
    val bar = BarEntity.builder()
      .aint(1)
      .along(2)
      .build();
    fe.setBar(bar);
    System.out.println(fe);
  }
}
