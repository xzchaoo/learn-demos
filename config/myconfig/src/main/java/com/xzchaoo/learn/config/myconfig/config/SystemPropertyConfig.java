package com.xzchaoo.learn.config.myconfig.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * TODO 系统属性是会变的, 但我们没法监听变化, 所以就认为它是不变的
 *
 * @author xzchaoo
 * @date 2018/5/25
 */
public class SystemPropertyConfig extends MapConfig {
  public static final SystemPropertyConfig INSTANCE = new SystemPropertyConfig();

  public SystemPropertyConfig() {
    super(convertToMap(System.getProperties()));
  }

  private static Map<String, String> convertToMap(Properties properties) {
    if (properties == null) {
      return Collections.emptyMap();
    }
    Map<String, String> map = new HashMap<>();
    for (Map.Entry<Object, Object> e : properties.entrySet()) {
      map.put(e.getKey().toString(), e.getValue().toString());
    }
    return map;
  }

  @Override
  public void replace(Map<String, String> map) {
    throw new UnsupportedOperationException();
  }
}
