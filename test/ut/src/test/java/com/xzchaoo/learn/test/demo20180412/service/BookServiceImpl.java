package com.xzchaoo.learn.test.demo20180412.service;

import com.xzchaoo.learn.test.demo20180412.dao.BookDao;
import com.xzchaoo.learn.test.demo20180412.model.Book;
import com.xzchaoo.learn.test.demo20180412.utils.CollectionUtils;

import java.util.List;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public class BookServiceImpl implements BookService {
  private BookDao bookDao;
  private BookDao bakupBookDao;

  @Override
  public List<Book> listAllBooks() {
    List<Book> books = bookDao.listAllBooks();
    if (CollectionUtils.isEmpty(books)) {
      books = bakupBookDao.listAllBooks();
    }
    return books;
  }

  @Override
  public int getTotalPrice(List<Book> books) {
    if (CollectionUtils.isEmpty(books)) {
      return 0;
    }
    int sum = 0;
    for (Book book : books) {
      int price = getPrice(book);
      sum += price;
    }
    return sum;
  }

  private int getPrice(Book book) {
    System.exit(1);
    return book.getPrice();
  }
}
