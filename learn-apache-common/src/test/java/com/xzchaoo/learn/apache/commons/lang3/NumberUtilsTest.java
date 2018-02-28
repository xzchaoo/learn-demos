package com.xzchaoo.learn.apache.commons.lang3;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class NumberUtilsTest {
  @Test
  public void test_min() {
    assertThat(NumberUtils.min(1, 2, 3, 4)).isEqualTo(1);
  }

  @Test
  public void test_compare() {
    assertThat(NumberUtils.compare(1, 2)).isEqualTo(-1);
  }
}
