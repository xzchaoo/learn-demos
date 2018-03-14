package com.xzchaoo.learn.guava;

import com.google.common.base.Joiner;

import org.junit.Test;

/**
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class JoinerTest {
  @Test
  public void test() {
    //构造出来之后就是线程安全的了
    //每次的构造都会创建一个joiner 所以最好做成静态变量 不要每次都创建
    Joiner joiner = Joiner.on('-')
      .skipNulls();
  }
}
