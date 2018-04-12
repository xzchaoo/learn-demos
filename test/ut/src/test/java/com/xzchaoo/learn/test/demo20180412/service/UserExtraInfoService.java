package com.xzchaoo.learn.test.demo20180412.service;

import com.xzchaoo.learn.test.demo20180412.model.User;

import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public class UserExtraInfoService {
  public void fillExtraInfo1(User user) {
    if (user == null) {
      throw new IllegalArgumentException();
    }
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(user.getUsername().getBytes(StandardCharsets.UTF_8));
      byte[] bytes = md5.digest();
      String result = Hex.encodeHexString(bytes);
      user.setExtraInfo1(result);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public void fillExtraInfo2(User user) {
    System.exit(1);
    user.setExtraInfo2("ex2");
  }
}
