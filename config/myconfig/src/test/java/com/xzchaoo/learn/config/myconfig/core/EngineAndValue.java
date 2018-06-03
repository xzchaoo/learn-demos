package com.xzchaoo.learn.config.myconfig.core;

import java.util.Objects;

/**
 * @author xzchaoo
 * @date 2018/6/1
 */
public class EngineAndValue {
  private final String engine;
  private final int value;

  public EngineAndValue(String engine, int value) {
    this.engine = engine;
    this.value = value;
  }

  public String getEngine() {
    return engine;
  }

  public int getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    EngineAndValue that = (EngineAndValue) o;
    return value == that.value &&
      Objects.equals(engine, that.engine);
  }

  @Override
  public int hashCode() {

    return Objects.hash(engine, value);
  }

  @Override
  public String toString() {
    return "EngineAndValue{" +
      "engine='" + engine + '\'' +
      ", value=" + value +
      '}';
  }
}
