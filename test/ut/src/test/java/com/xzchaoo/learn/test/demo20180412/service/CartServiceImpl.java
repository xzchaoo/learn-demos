package com.xzchaoo.learn.test.demo20180412.service;

import com.xzchaoo.learn.test.demo20180412.model.Book;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public class CartServiceImpl implements CartService {
  private ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>();

  @Override
  public void add(int userId, Book book) {

  }
}
