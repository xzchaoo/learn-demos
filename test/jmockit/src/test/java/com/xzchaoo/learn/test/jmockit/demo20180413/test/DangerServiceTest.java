package com.xzchaoo.learn.test.jmockit.demo20180413.test;

import com.xzchaoo.learn.test.jmockit.demo20180413.service.DangerService;
import com.xzchaoo.learn.test.jmockit.demo20180413.service.DangerTask;

import org.junit.Test;

import mockit.Mock;
import mockit.MockUp;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class DangerServiceTest {
  @Test
  public void test_new() {
    new MockUp<DangerTask>() {
      @Mock
      void $init() {
      }
    };
    new DangerTask();
  }

  @Test
  public void test_run() {
    new MockUp<DangerTask>() {
      @Mock
      void $init() {
      }
    };
    new DangerService().run();
  }
}
