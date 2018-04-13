package com.xzchaoo.learn.test.demo20180413.powermock;

import com.xzchaoo.learn.test.demo20180413.service.StaticService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(StaticService.class)
public class StaticServiceTest {

  @Test
  public void test_callPrivateStaticMethod() throws Exception {
    PowerMockito.spy(StaticService.class);
    PowerMockito.doReturn("foo")
      .when(StaticService.class, "privateStaticMethod", anyInt(), anyInt());
    assertThat(StaticService.callPrivateStaticMethod(1, 2)).isEqualTo("foo");
  }

  @Test
  public void test_publicStaticMethod() throws Exception {
    PowerMockito.mockStatic(StaticService.class);
    // right
    // PowerMockito.doAnswer(invocation -> {
    //   int a = invocation.getArgument(0);
    //   int b = invocation.getArgument(1);
    //   return Integer.toString(a) + Integer.toString(b);
    // }).when(StaticService.class, "publicStaticMethod", anyInt(), anyInt());

    // error
    PowerMockito.doAnswer(invocation -> {
      int a = invocation.getArgument(0);
      int b = invocation.getArgument(1);
      return Integer.toString(a) + Integer.toString(b);
    }).when(StaticService.publicStaticMethod(anyInt(), anyInt()));

    // right
    // PowerMockito.when(StaticService.publicStaticMethod(anyInt(), anyInt()))
    //   .thenAnswer(invocation -> {
    //     int a = invocation.getArgument(0);
    //     int b = invocation.getArgument(1);
    //     return Integer.toString(a) + Integer.toString(b);
    //   });

    assertThat(StaticService.publicStaticMethod(1, 2)).isEqualTo("12");
  }
}
