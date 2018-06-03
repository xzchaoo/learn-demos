package com.xzchaoo.learn.config.myconfig.core.property;

import java.util.Objects;

/**
 * @author xzchaoo
 * @date 2018/6/3
 */
public class PropertyKey<T> {
  private final String key;
  private final Class<T> clazz;

  public PropertyKey(String key, Class<T> clazz) {
    this.key = key;
    this.clazz = clazz;
  }

  public String getKey() {
    return key;
  }

  public Class<T> getClazz() {
    return clazz;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PropertyKey<?> that = (PropertyKey<?>) o;
    return Objects.equals(key, that.key) &&
      Objects.equals(clazz, that.clazz);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, clazz);
  }

  @Override
  public String toString() {
    return "PropertyKey{" +
      "key='" + key + '\'' +
      ", clazz=" + clazz +
      '}';
  }
}
