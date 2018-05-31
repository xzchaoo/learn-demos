package com.xzchaoo.learn.config.myconfig;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public interface IFooConfig {
  String getString(String key);

  String getString(String key, String defaultValue);

  Integer getInteger(String key);

  int getInteger(String key, int defaultValue);

  Long getLong(String key);

  long getLong(String key, long defaultValue);

  <T> List<T> getList(String key);

  <T> List<T> getList(String key, Supplier<? extends List<T>> defaultValueProvider);

  <K, V> Map<K, V> getMap(String key);

  <K, V> Map<K, V> getMap(String key, Supplier<? extends Map<K, V>> defaultValueProvider);
}
