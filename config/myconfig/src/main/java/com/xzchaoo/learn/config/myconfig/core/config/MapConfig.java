package com.xzchaoo.learn.config.myconfig.core.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于map实现的config
 *
 * @author xzchaoo
 * @date 2018/5/25
 */
public class MapConfig extends AbstractConfig {
  public static class Builder {
    private final Map<String, String> map = new HashMap<>();

    public Builder put(String name, String value) {
      map.put(name, value);
      return this;
    }

    public MapConfig build() {
      return new MapConfig(map);
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  protected volatile Map<String, String> map;

  public MapConfig() {
    this(null);
  }

  public MapConfig(Map<String, String> map) {
    internalSetMap(map);
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
  public Map<String, String> asMap() {
    return map;
  }

  @Override
  public boolean contains(String key) {
    return map.containsKey(key);
  }

  public synchronized void replace(Map<String, String> map) {
    internalSetMap(map);
    notifyChanged();
  }

  /**
   * 如果提供的map为null 则用空map代替, 否则建立一个不可变的副本
   *
   * @param map 新的配置
   */
  private void internalSetMap(Map<String, String> map) {
    this.map = map == null ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<>(map));
  }

  @Override
  public String toString() {
    return map.toString();
  }
}
