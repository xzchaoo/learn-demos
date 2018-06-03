package com.xzchaoo.learn.config.myconfig.core.property;


import java.util.function.Supplier;

/**
 * @author xzchaoo
 * @date 2018/6/3
 */
public interface Property<T> extends Supplier<T> {
  /**
   * 获取配置的key
   *
   * @return key
   */
  String getKey();

  /**
   * 获取原始的配置字符串
   *
   * @return 原始的配置字符串
   */
  String getRawString();

  /**
   * 获取配置值
   *
   * @return
   */
  @Override
  T get();

  /**
   * 添加监听器
   *
   * @param listener 监听器
   */
  void addListener(PropertyChangeListener<T> listener);

  /**
   * 移除监听器
   *
   * @param listener 监听器
   */
  void removeListener(PropertyChangeListener<T> listener);
}
