package com.xzchaoo.learn.test.demo20180412.service;

import com.xzchaoo.learn.test.demo20180412.model.Book;

import java.util.List;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public interface BookService {
  List<Book> listAllBooks();

  int getTotalPrice(List<Book> books);
}
