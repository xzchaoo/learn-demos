package com.xzchaoo.learn.apache.commons.lang3;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringUtilsTest {

  @Test
  public void test_remove() {
    System.out.println(StringUtils.remove("baaac", "a"));
  }

  @Test
  public void test_replace() {
    String str = "a=b;c=d;a=b;";
    //会替换全部
    String result = StringUtils.replace(str, "a=b;", "");
    assertThat(result).isEqualTo("c=d;");
  }

  @Test
  public void test_substringBefore() {
    String s = StringUtils.substringBefore("abab", "b");
    assertThat(s).isEqualTo("a");
  }

  @Test
  public void test_split() {
    String[] ss = StringUtils.split("|||a|b|c", "|");
    assertThat(ss).containsExactly("a", "b", "c");
  }

  @Test
  public void test_empty() {
    assertThat(StringUtils.isEmpty(null)).isTrue();
    assertTrue(StringUtils.isEmpty(""));
    assertFalse(StringUtils.isEmpty(" "));

    assertTrue(StringUtils.isBlank(null));
    assertTrue(StringUtils.isBlank(" \t"));
  }

  @Test
  public void test_pad() {
    assertEquals("**xzc", StringUtils.leftPad("xzc", 5, "*"));
  }
}
