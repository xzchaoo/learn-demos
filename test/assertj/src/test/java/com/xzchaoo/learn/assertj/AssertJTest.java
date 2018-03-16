package com.xzchaoo.learn.assertj;

import org.assertj.core.api.AssertDelegateTarget;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.assertj.core.api.JUnitSoftAssertions;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.util.Lists;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;

/**
 * 介绍断言工具 assertj 的用法
 * 合理使用 assertj 可以让你的断言工作变得更流畅
 *
 * @author xzchaoo
 * @date 2018/1/1
 */
public class AssertJTest {
  @Test
  public void test_simple() {
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
  public void test_array() {
    //match可以让你传入一个predicate
    //satisfy可以让你进行
    List<Integer> list1 = Lists.newArrayList(1, 2, 3, 4);
    assertThat(list1)
      .contains(1, 2, 3)
      .containsOnly(4, 1, 2, 3)
      .containsExactly(1, 2, 3, 4)
      .anySatisfy(x -> {
        assertThat(x * x).isEqualTo(4);
      }).anyMatch(x -> x * x == 4);
    assertThat(list1).filteredOn(x -> x % 2 == 0).containsOnly(2, 4);
    assertThat(list1).filteredOn(x -> x % 2 == 1).containsOnly(1, 3);

    assertThat(list1)
      .filteredOn(i -> (i & 1) == 0)
      .extracting(Object::toString)
      .containsOnly("2", "4")
      .doesNotContain("0");
  }

  @Test
  public void test_exception() {
    assertThatThrownBy(() -> {
      throw new RuntimeException("abc");
    }).isInstanceOf(RuntimeException.class)
      //全匹配
      .hasMessageContaining("ab")
      .hasMessage("ab%s", "c");

    assertThatCode(() -> {
    }).doesNotThrowAnyException();

    Throwable throwable = catchThrowable(() -> {
      throw new RuntimeException();
    });
    assertThat(throwable).isInstanceOf(RuntimeException.class);
  }

  @Rule
  public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

  @Test
  public void test_soft() {
    //可以避免快速失败
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(1).as("1").isEqualTo(1);
    softly.assertThat(2).as("2").isEqualTo(2);
    softly.assertAll();
  }

  @Test
  public void test_soft2() {
    //可以避免快速失败
    softly.assertThat(1).as("1").isEqualTo(1);
    softly.assertThat(2).as("2").isEqualTo(2);
    //这个就不用assertAll了 softly.assertAll();
  }

  @Test
  public void test_soft3() {
    //还可以这样
    try ( AutoCloseableSoftAssertions softly = new AutoCloseableSoftAssertions() ) {
      softly.assertThat(1).as("1").isEqualTo(1);
      softly.assertThat(2).as("2").isEqualTo(2);
    }
  }


  @Test
  public void test_soft4() {
    SoftAssertions.assertSoftly(softly -> {
      softly.assertThat(1).as("1").isEqualTo(1);
      softly.assertThat(2).as("2").isEqualTo(2);
    });
  }

  @Test
  public void test_comparator() {
    Comparator<User> comparator = Comparator.comparingInt(User::getId);
    assertThat(new User(1)).usingComparator(comparator).isEqualTo(new User(1));
  }

  @Test
  public void test_assertThatThrownBy() {
    assertThatThrownBy(() -> {
      throw new RuntimeException("aaa");
    }).hasMessageContaining("aa");
  }

  private static class MyObjectAssert implements AssertDelegateTarget {
    Object object;

    MyObjectAssert(Object object) {
      this.object = object;
    }

    void isNotNull() {
      assertThat(object).isNotNull();
    }
  }

  public void test_MyObjectAssert() {
  }
}
