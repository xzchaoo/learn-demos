package com.xzchaoo.learn.test.jmockit.demo20180413.test;

import com.xzchaoo.learn.test.jmockit.demo20180413.service.StaticInitService;

import org.junit.Test;

import mockit.Mock;
import mockit.MockUp;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class StaticInitServiceTest {
  @Test
  public void test() {
    new MockUp<StaticInitService>() {
      @Mock
      void $clinit() {
      }
    };
    assertThat(new StaticInitService().getValue()).isEqualTo("success");
  }
}
