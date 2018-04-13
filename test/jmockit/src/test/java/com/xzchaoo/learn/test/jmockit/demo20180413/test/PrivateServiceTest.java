package com.xzchaoo.learn.test.jmockit.demo20180413.test;

import com.xzchaoo.learn.test.jmockit.demo20180413.service.PrivateService;

import org.junit.Test;

import mockit.Mock;
import mockit.MockUp;
import mockit.Tested;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class PrivateServiceTest {
  @Tested
  PrivateService privateService;

  @Test
  public void test() {
    new MockUp<PrivateService>() {
      @Mock
      int privateMethod(int a, int b) {
        return a + b;
      }
    };
    assertThat(privateService.callPrivateMethod(1, 2)).isEqualTo(3);
  }
}
