package com.xzchaoo.learn.config.myconfig.biz.parsers;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class ConfigConfig {
  private Map<Field, PropertyConfig<?>> fieldPropertyConfigs = new HashMap<>();

  public void addFieldProperty(Field field, PropertyConfig<?> propertyConfig) {
    fieldPropertyConfigs.put(field, propertyConfig);
  }

  public Map<Field, PropertyConfig<?>> getFieldPropertyConfigs() {
    return fieldPropertyConfigs;
  }
}
