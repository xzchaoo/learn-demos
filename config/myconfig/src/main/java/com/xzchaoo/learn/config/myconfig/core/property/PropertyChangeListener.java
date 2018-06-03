package com.xzchaoo.learn.config.myconfig.core.property;

/**
 * @author xzchaoo
 * @date 2018/6/3
 */
public interface PropertyChangeListener<T> {
  /**
   * @param property 属性
   * @param oldValue 旧值
   * @param newValue 新值
   */
  void onPropertyChange(PropertyContainer<T> property, T oldValue, T newValue);
}
