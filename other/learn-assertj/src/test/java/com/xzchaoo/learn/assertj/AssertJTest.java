package com.xzchaoo.learn.assertj;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author xzchaoo
 * @date 2018/1/1
 */
public class AssertJTest {
  @Test
  public void test() {
    String name = "xzc";
    //assertThat其实有很多重载的!
    assertThat("xzc")
      .as("检查姓名")//当assert失败的时候回有一些提示
      .hasSize(3)
      .startsWith("x")
      .endsWith("c")
      .doesNotEndWith("ZC")
      .isEqualToIgnoringCase("XZC");

    assertThat(name)
      .hasSize(3)
      .contains("zc")
      .doesNotContain("F")
      .startsWith("x")
      .endsWith("c")
      .isEqualTo("xzc")
      .isEqualToIgnoringCase("XZC");

    assertThat(Arrays.asList("a", "b", "c")).filteredOn(x -> x.equals("a")).containsOnlyOnce("a");
  }

  @Test
  public void test_assertThatThrownBy() {
    assertThatThrownBy(() -> {
      throw new RuntimeException("aaa");
    }).hasMessageContaining("aa");
  }
}
