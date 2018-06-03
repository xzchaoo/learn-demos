package com.xzchaoo.learn.config.myconfig.core.property;


import java.util.function.Supplier;

/**
 * @author xzchaoo
 * @date 2018/6/3
 */
public interface PropertyContainer<T> extends Supplier<T> {
  String getKey();

  String getRawString();

  void addListener(PropertyChangeListener<T> listener);

  void removeListener(PropertyChangeListener<T> listener);
}
