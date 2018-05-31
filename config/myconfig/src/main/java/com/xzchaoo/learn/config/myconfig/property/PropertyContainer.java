package com.xzchaoo.learn.config.myconfig.property;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class PropertyContainer {
  private ConcurrentHashMap<String, Property<?>> parserCache = new ConcurrentHashMap<>();
  private ConcurrentLinkedQueue<PropertyImpl<?>> properties = new ConcurrentLinkedQueue<>();

  public <T> Property<T> get(String key) {
    // 初始化的值?
    PropertyImpl<T> p = new PropertyImpl<>(key, null, null);
    Property<?> property = parserCache.get(key);

    // p.onChange();
    return p;
  }

  public synchronized void onRefresh(Map<String, String> configMap) {
    for (PropertyImpl<?> p : properties) {
      String value = configMap.get(p.getKey());
      p.onChange(value);
    }
  }
}
