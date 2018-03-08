package com.xzchaoo.learn.test.powermock.ignore;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
public class FooUtils {
  public static final String a;

  static {
    a = "a";
  }

  public static String encode(String text, String encoding) throws UnsupportedEncodingException {
    return URLEncoder.encode(text, encoding);
  }
}
