package com.xzchaoo.learn.jdk8;

import org.junit.Test;

/**
 * @author zcxu
 * @date 2018/3/13 0013
 */
public class EnumTest {
  public enum Gender {
    MALe, FEMALE
  }

  @Test
  public void test() {
    //enum是需要完全匹配的
    //Gender.valueOf("MALE") error
  }
}
