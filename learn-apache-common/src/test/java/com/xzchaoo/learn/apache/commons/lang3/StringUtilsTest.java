package com.xzchaoo.learn.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class StringUtilsTest {

  @Test
  public void test_split() {
    String[] ss = StringUtils.split("|||a|b|c", "|");
    System.out.println(Arrays.asList(ss));
  }

  @Test
  public void test_empty() {
    assertTrue(StringUtils.isEmpty(null));
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
