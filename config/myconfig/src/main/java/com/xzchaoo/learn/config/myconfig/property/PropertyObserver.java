package com.xzchaoo.learn.config.myconfig.property;

/**
 * @author xzchaoo
 * @date 2018/5/25
 */
@FunctionalInterface
public interface PropertyObserver<T> {
  void onChange(T newValue, T oldValue);
}
