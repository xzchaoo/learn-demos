package com.xzchaoo.learn.config.myconfig.core.parser;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 描述一个配置实例
 *
 * @author xzchaoo
 * @date 2018/5/31
 */
class PojoDescription {
  private final Class<?> clazz;
  private final Map<Field, PropertyDescription<?>> fieldPropertyConfigs;

  PojoDescription(Class<?> clazz, Map<Field, PropertyDescription<?>> fieldPropertyConfigs) {
    this.clazz = clazz;
    this.fieldPropertyConfigs = fieldPropertyConfigs;
  }

  Class<?> getClazz() {
    return clazz;
  }

  Map<Field, PropertyDescription<?>> getFieldPropertyConfigs() {
    return fieldPropertyConfigs;
  }
}
