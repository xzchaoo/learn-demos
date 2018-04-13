package com.xzchaoo.learn.test.demo20180413.powermock;

import com.xzchaoo.learn.test.demo20180413.service.BookService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.doReturn;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor("com.xzchaoo.learn.test.demo20180413.service.BookService")
public class BookServiceTest3 {
  @InjectMocks
  @Spy
  BookService service = new BookService();

  @Test
  public void test_add2() throws Exception {
    doReturn(2).when(service, "getValue2");
    assertThat(service.add2(3)).isEqualTo(5);
  }
}
