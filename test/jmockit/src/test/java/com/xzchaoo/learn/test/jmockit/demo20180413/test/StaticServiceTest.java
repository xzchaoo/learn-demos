package com.xzchaoo.learn.test.jmockit.demo20180413.test;

import com.xzchaoo.learn.test.jmockit.demo20180413.service.StaticService;

import org.junit.Test;

import mockit.Mock;
import mockit.MockUp;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class StaticServiceTest {
  @Test
  public void test_publicStaticMethod() {
    new MockUp<StaticService>() {
      @Mock
      int publicStaticMethod(int a, int b) {
        return a + b;
      }
    };
    assertThat(StaticService.publicStaticMethod(1, 2)).isEqualTo(3);
  }

  @Test
  public void test_callPrivateStaticMethod() {
    new MockUp<StaticService>() {
      @Mock
      int privateStaticMethod(int a, int b) {
        return a + b;
      }
    };
    assertThat(StaticService.callPrivateStaticMethod(1, 2)).isEqualTo(3);
  }
}
