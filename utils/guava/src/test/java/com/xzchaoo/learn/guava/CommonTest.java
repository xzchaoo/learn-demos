package com.xzchaoo.learn.guava;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/6/20
 */
public class CommonTest {
  @Test
  public void test_splitter() {
    final Splitter splitter = Splitter.on('-').trimResults();
    assertThat(splitter.splitToList("1-2-3-4")).containsOnly("1", "2", "3", "4");
  }

  @Test
  public void test_joiner() {
    //构造出来之后就是线程安全的了
    //每次的构造都会创建一个joiner 所以最好做成静态变量 不要每次都创建
    Joiner joiner = Joiner.on('-')
      .skipNulls();
    assertThat(joiner.join(Arrays.asList(1, 2, 3, 4))).isEqualTo("1-2-3-4");
  }
}
