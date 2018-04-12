package com.xzchaoo.learn.test.demo20180412.test;

import com.xzchaoo.learn.test.demo20180412.dao.BookDao;
import com.xzchaoo.learn.test.demo20180412.model.Book;
import com.xzchaoo.learn.test.demo20180412.service.BookServiceImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
@RunWith(MockitoJUnitRunner.class)
public class BookServiceImplTestEx {
  @InjectMocks
  BookServiceImpl service;
  @Mock
  BookDao bookDao;
  @Mock
  BookDao bakupBookDao;

  @Test
  public void test_listAllBooks_1() {
    Book b1 = new Book();
    when(bookDao.listAllBooks()).thenReturn(singletonList(b1));
    List<Book> books = service.listAllBooks();
    assertThat(books).containsOnly(b1);
  }

  @Test
  public void test_listAllBooks_2() {
    Book b1 = new Book();
    when(bookDao.listAllBooks()).thenReturn(null);
    when(bakupBookDao.listAllBooks()).thenReturn(singletonList(b1));
    List<Book> books = service.listAllBooks();
    verify(bookDao).listAllBooks();
    assertThat(books).containsOnly(b1);
  }
}
