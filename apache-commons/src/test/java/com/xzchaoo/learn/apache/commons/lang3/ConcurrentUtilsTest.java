package com.xzchaoo.learn.apache.commons.lang3;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Test;

/**
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class ConcurrentUtilsTest {
  @Test
  public void test() {
    new BasicThreadFactory.Builder()
      .daemon(true)
      .priority(1)
      .uncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
        }
      })
      .build();
  }
}
