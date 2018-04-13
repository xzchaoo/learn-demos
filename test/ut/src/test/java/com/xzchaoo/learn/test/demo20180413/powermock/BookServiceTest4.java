package com.xzchaoo.learn.test.demo20180413.powermock;

import com.xzchaoo.learn.test.demo20180413.service.BookService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
@RunWith(PowerMockRunner.class)
@SuppressStaticInitializationFor({"com.xzchaoo.learn.test.demo20180413.service.BookService", "com.xzchaoo.learn.test.demo20180413.dao.BookDao"})
public class BookServiceTest4 {
  @Test
  public void test_add3() throws Exception {
    mockStatic(BookService.class);
    doAnswer(invocation -> {
      int arg = invocation.getArgument(0);
      return 3 + arg;
    }).when(BookService.class, "add3", anyInt());
    int value = Whitebox.invokeMethod(BookService.class, "add3", 1);
    assertThat(value).isEqualTo(4);
  }
}
