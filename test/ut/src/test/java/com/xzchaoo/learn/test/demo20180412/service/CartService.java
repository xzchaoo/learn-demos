package com.xzchaoo.learn.test.demo20180412.service;

import com.xzchaoo.learn.test.demo20180412.model.Book;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public interface CartService {
  void add(int userId, Book book);
}
