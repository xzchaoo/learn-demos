package com.xzchaoo.learn.config.myconfig.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xzchaoo
 * @date 2018/5/25
 */
public class MapConfig extends AbstractConfig {
  protected Map<String, String> map;

  public MapConfig(Map<String, String> map) {
    if (map == null) {
      throw new NullPointerException();
    }
    this.map = Collections.unmodifiableMap(new HashMap<>(map));
  }

  @Override
  public String getString(String key) {
    return map.get(key);
  }

  @Override
  public String getString(String key, String defaultValue) {
    return map.getOrDefault(key, defaultValue);
  }

  @Override
  public Map<String, String> getAsMap() {
    return map;
  }

  @Override
  public boolean contains(String key) {
    return map.containsKey(key);
  }

  public synchronized void replace(Map<String, String> map) {
    if (map == null) {
      throw new NullPointerException();
    }
    this.map = Collections.unmodifiableMap(new HashMap<>(map));
    triggerChange();
  }
}
