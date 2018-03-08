package com.xzchaoo.learn.apache.commons.lang3;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 提供了对 Date 对象的辅助方法
 *
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class DateUtilsTest {
  @Test
  public void test_StopWatch() {
    StopWatch sw = StopWatch.createStarted();
    assertThat(sw.isStarted()).isTrue();
    sw.stop();
    sw.reset();

    sw.start();
    sw.stop();
  }
}
