package com.xzchaoo.learn.config.myconfig.core.parser;


import java.lang.reflect.Field;
import java.util.Map;

/**
 * 对一个pojo及其属性的封装
 *
 * @author xzchaoo
 * @date 2018/5/31
 */
@SuppressWarnings("WeakerAccess")
class PojoWrapper {
  private final Object pojo;
  private final Class<?> clazz;
  private final Map<Field, PropertyDescription<?>> fieldPropertyConfigs;

  PojoWrapper(Object pojo, Class<?> clazz, Map<Field, PropertyDescription<?>> fieldPropertyConfigs) {
    this.pojo = pojo;
    this.clazz = clazz;
    this.fieldPropertyConfigs = fieldPropertyConfigs;
  }

  Object getPojo() {
    return pojo;
  }

  Class<?> getClazz() {
    return clazz;
  }

  Map<Field, PropertyDescription<?>> getFieldPropertyConfigs() {
    return fieldPropertyConfigs;
  }
}
