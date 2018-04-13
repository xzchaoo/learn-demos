package com.xzchaoo.learn.test.demo20180413.powermock;

import com.xzchaoo.learn.test.demo20180413.service.BookService;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"com.xzchaoo.learn.test.demo20180413.service.BookService"})
@PrepareForTest(fullyQualifiedNames = {"org.apache.commons.lang3.StringUtils"})
public class BookServiceTest2 {
  @InjectMocks
  BookService service;

  @Test
  public void test_isEmpty() {
    PowerMockito.mockStatic(StringUtils.class);
    when(StringUtils.isEmpty("foo")).thenReturn(true);
    assertThat(service.isEmpty("foo")).isTrue();
  }
}
