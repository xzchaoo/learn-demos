package com.xzchaoo.learn.test.demo20180413.powermock;

import com.xzchaoo.learn.test.demo20180413.service.FinalService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FinalService.class)
public class FinalServiceTest {

  @Test
  public void test_publicFinalMethod() {
    FinalService service = PowerMockito.mock(FinalService.class);
    when(service.publicFinalMethod()).thenReturn("foo");
    assertThat(service.publicFinalMethod()).isEqualTo("foo");
  }

  @Test
  public void test_callFinalMethod() throws Exception {
    FinalService service = new FinalService();
    FinalService serviceSpy = PowerMockito.spy(service);
    doReturn("OK").when(serviceSpy, "finalMethod");
    assertThat(serviceSpy.callFinalMethod()).isEqualTo("OK");
  }
}
