package com.xzchaoo.learn.test.demo20180412.test;

import com.xzchaoo.learn.test.demo20180412.model.Book;
import com.xzchaoo.learn.test.demo20180412.service.BookServiceImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.doAnswer;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BookServiceImpl.class)
public class BookServiceImplTest {
  @InjectMocks
  @Spy
  BookServiceImpl service = new BookServiceImpl();

  @Test
  public void test_getTotalPrice() throws Exception {
    Book b1 = new Book();
    b1.setPrice(7);
    Book b2 = new Book();
    b2.setPrice(8);
    doAnswer(invocation -> {
      Book b = invocation.getArgument(0);
      return b.getPrice();
    }).when(service, "getPrice", any(Book.class));
    int sum = service.getTotalPrice(asList(b1, b2));
    assertThat(sum).isEqualTo(15);
  }
}
