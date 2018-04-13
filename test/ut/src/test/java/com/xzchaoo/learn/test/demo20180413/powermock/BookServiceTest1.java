package com.xzchaoo.learn.test.demo20180413.powermock;

import com.xzchaoo.learn.test.demo20180413.dao.BookDao;
import com.xzchaoo.learn.test.demo20180413.service.BookService;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
@SuppressStaticInitializationFor({"com.xzchaoo.learn.test.demo20180413.service.BookService", "com.xzchaoo.learn.test.demo20180413.dao.BookDao"})
//@PrepareForTest(value = {BookService.class}, fullyQualifiedNames = {"org.apache.commons.lang3.StringUtils"})
@PrepareForTest(fullyQualifiedNames = {"org.apache.commons.lang3.StringUtils"})
public class BookServiceTest1 {
  @InjectMocks
  BookService service = new BookService();

  @Mock
  BookDao bookDao;

  @Test
  public void test_static() {
    // 静态构造函数被镇压了
    assertThat(BookService.STATIC_1).isEqualTo("1");
    assertThat(BookService.STATIC_2).isNull();
    assertThat(BookService.STATIC_3).isNull();
  }

  @Test
  public void test_getString() {
    PowerMockito.mockStatic(BookDao.class);
    when(BookDao.getInstance()).thenReturn(bookDao);
    when(bookDao.toString()).thenReturn("book");
    String result = service.getString("x");
    assertThat(result).isEqualTo("bookx");
  }

  @Test
  public void test_isEmpty() {
    PowerMockito.mockStatic(StringUtils.class);
    when(StringUtils.isEmpty("foo")).thenReturn(true);
    assertThat(service.isEmpty("foo")).isTrue();
  }

}
