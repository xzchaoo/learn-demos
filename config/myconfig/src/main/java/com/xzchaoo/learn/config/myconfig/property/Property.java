package com.xzchaoo.learn.config.myconfig.property;

/**
 * @author xzchaoo
 * @date 2018/5/25
 */
public interface Property<T> {
  String getKey();

  T get();

  Property<T> withDefault(T defaultValue);
}
