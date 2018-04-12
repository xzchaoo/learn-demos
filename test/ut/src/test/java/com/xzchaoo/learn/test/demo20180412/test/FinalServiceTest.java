package com.xzchaoo.learn.test.demo20180412.test;

import com.xzchaoo.learn.test.demo20180412.service.FinalService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(FinalService.class)
public class FinalServiceTest {

  @Test
  public void test() {
    FinalService service = PowerMockito.mock(FinalService.class);
    when(service.finalMethod()).thenReturn("OK");
    assertThat(service.finalMethod()).isEqualTo("OK");
  }
}
