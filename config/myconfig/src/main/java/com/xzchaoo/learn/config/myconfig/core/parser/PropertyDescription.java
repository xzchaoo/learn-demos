package com.xzchaoo.learn.config.myconfig.core.parser;

import java.lang.reflect.Field;

/**
 * 描述一个配置的属性
 *
 * @author xzchaoo
 * @date 2018/5/31
 */
@SuppressWarnings("WeakerAccess")
public class PropertyDescription<T> {
  private final String key;
  private final Field field;
  private final Parser<T> parser;
  private final String defaultValue;

  public PropertyDescription(String key, Field field, Parser<T> parser, String defaultValue) {
    this.key = key;
    this.field = field;
    this.parser = parser;
    this.defaultValue = defaultValue;
  }

  public String getKey() {
    return key;
  }

  public Field getField() {
    return field;
  }

  public Parser<T> getParser() {
    return parser;
  }

  public String getDefaultValue() {
    return defaultValue;
  }
}
