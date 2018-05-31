package com.xzchaoo.learn.config.myconfig.property;

import java.util.function.Function;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class PropertyImpl<T> implements Property<T> {
  private volatile T cache;
  private final Function<String, ? extends T> valueParser;
  private final String key;
  private final T defaultValue;

  public PropertyImpl(String key, Function<String, ? extends T> valueParser, T defaultValue) {
    this.key = key;
    this.valueParser = valueParser;
    this.defaultValue = defaultValue;
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public T get() {
    T cache = this.cache;
    T defaultValue = this.defaultValue;
    return cache == null ? defaultValue : cache;
  }

  @Override
  public Property<T> withDefault(T defaultValue) {
    return new PropertyImpl<>(key, valueParser, defaultValue);
  }

  // ?
  public void onChange(String str) {
    T value = valueParser.apply(str);
    this.cache = value;
  }
}
