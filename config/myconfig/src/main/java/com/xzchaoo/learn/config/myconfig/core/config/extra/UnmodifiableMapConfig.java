package com.xzchaoo.learn.config.myconfig.core.config.extra;

import com.xzchaoo.learn.config.myconfig.core.ConfigChangeListener;
import com.xzchaoo.learn.config.myconfig.core.config.AbstractConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author xzchaoo
 * @date 2018/6/2
 */
public abstract class UnmodifiableMapConfig extends AbstractConfig {

  private final Map<String, String> map;

  protected static Map<String, String> convertToMap(Properties properties) {
    if (properties == null) {
      return Collections.emptyMap();
    }
    Map<String, String> map = new HashMap<>();
    for (Map.Entry<Object, Object> e : properties.entrySet()) {
      map.put(e.getKey().toString(), e.getValue().toString());
    }
    return map;
  }

  protected UnmodifiableMapConfig(String name, Map<String, String> map) {
    super(name);
    this.map = map == null ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<>(map));
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
  public boolean contains(String key) {
    return map.containsKey(key);
  }

  @Override
  public Map<String, String> asMap() {
    return map;
  }

  @Override
  public void addListener(ConfigChangeListener listener) {
  }

  @Override
  public void removeListener(ConfigChangeListener listener) {
  }
}
