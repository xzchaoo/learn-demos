package com.xzchaoo.learn.test.jmockit.demo20180413.test;

import com.xzchaoo.learn.test.jmockit.demo20180413.service.FinalService;

import org.junit.Test;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class FinalServiceTest {
  @Injectable
  FinalService finalService;

  @Test
  public void test() {
    new Expectations() {{
      finalService.publicFinalMethod(anyInt, anyInt);
      result = new Delegate<Integer>() {
        int publicFinalMethod(int a, int b) {
          return a + b;
        }
      };
    }};
    assertThat(finalService.publicFinalMethod(1, 2)).isEqualTo(3);
  }
}
