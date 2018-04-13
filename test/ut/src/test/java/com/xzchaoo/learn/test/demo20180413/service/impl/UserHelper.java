package com.xzchaoo.learn.test.demo20180413.service.impl;

import com.xzchaoo.learn.test.demo20180413.model.User;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author xzchaoo
 * @date 2018/4/13
 */
public class UserHelper {
  void fillExtraInfo1(User user) {
    if (user == null) {
      return;
    }
    try {
      String username = user.getUsername();
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(username.getBytes(StandardCharsets.UTF_8));
      byte[] bytes = md5.digest();
      user.setExtraInfo1(Hex.encodeHexString(bytes));
    } catch (Exception e) {
      // ignore
      e.printStackTrace();
    }
  }
}
