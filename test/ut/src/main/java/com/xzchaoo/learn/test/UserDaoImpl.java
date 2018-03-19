/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.test;

public class UserDaoImpl implements UserDao {
  public User findUserByUserId(int userId) {
    User user = new User();
    user.setId(userId);
    user.setName("xzc " + userId);
    return user;
  }

  @Override
  public final void f1(int a) {

  }

  public final String f2() {
    return "f2";
  }
}
