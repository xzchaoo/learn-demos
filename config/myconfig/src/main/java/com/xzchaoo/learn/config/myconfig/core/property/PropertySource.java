package com.xzchaoo.learn.config.myconfig.core.property;

/**
 * @author xzchaoo
 * @date 2018/6/3
 */
public interface PropertySource {
  <T> PropertyContainer<T> getProperty(String key, Class<T> clazz, T defaultValue);
}
