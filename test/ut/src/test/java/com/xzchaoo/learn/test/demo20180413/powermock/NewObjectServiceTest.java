package com.xzchaoo.learn.test.demo20180413.powermock;

import com.xzchaoo.learn.test.demo20180413.service.NewObjectService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(NewObjectService.class)
public class NewObjectServiceTest {
  @InjectMocks
  NewObjectService service;

  @Test
  public void test() throws Exception {
    Object obj = mock(Object.class);
    PowerMockito.whenNew(Object.class).withAnyArguments().thenReturn(obj);
    when(obj.toString()).thenReturn("foo");
    assertThat(service.getString()).isEqualTo("foo");
  }
}
