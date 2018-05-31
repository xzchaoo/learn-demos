package com.xzchaoo.learn.config.myconfig.biz.parsers;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public interface Parser<T> {
  T parse(Class<T> clazz, String str);

  abstract class None implements Parser<Object> {
  }
}
