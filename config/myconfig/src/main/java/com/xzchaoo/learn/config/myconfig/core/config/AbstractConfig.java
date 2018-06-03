package com.xzchaoo.learn.config.myconfig.core.config;


import com.xzchaoo.learn.config.myconfig.core.Config;
import com.xzchaoo.learn.config.myconfig.core.ConfigChangeListener;

import org.assertj.core.util.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 为Config提供一个通用的抽象的实现
 *
 * @author xzchaoo
 * @date 2018/5/25
 */
public abstract class AbstractConfig implements Config {
  /**
   * 原子计数器, 用于生成随机的名字
   */
  private static final AtomicInteger COUNTER = new AtomicInteger(0);

  @SuppressWarnings("WeakerAccess")
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 该配置的名字
   */
  private final String name;


  /**
   * 用于存放所有观察者
   */
  private CopyOnWriteArrayList<ConfigChangeListener> listenerList = new CopyOnWriteArrayList<>();

  /**
   * 默认构造函数, 生成一个名字
   */
  public AbstractConfig() {
    this(null);
  }

  /**
   * 由调用方指定名字
   *
   * @param name
   */
  public AbstractConfig(String name) {
    if (name == null) {
      name = getClass().getSimpleName() + "-" + COUNTER.incrementAndGet();
    }
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void addListener(ConfigChangeListener listener) {
    Preconditions.checkNotNull(listener);
    listenerList.add(listener);
  }

  @Override
  public void removeListener(ConfigChangeListener listener) {
    Preconditions.checkNotNull(listener);
    listenerList.add(listener);
  }

  /**
   * 通知配置发生变化
   */
  @SuppressWarnings("WeakerAccess")
  protected void notifyChanged() {
    for (ConfigChangeListener listener : listenerList) {
      try {
        listener.onChange(this);
      } catch (Exception e) {
        logger.error("更新配置失败 {}", listener, e);
      }
    }
  }

  @Override
  public Boolean getBoolean(String key) {
    String value = getString(key);
    return value == null ? null : Boolean.parseBoolean(value);
  }

  @Override
  public boolean getBoolean(String key, boolean defaultValue) {
    String value = getString(key);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Boolean.parseBoolean(value);
    } catch (Exception e) {
      logger.warn("fail to parse {} as boolean", value);
      return defaultValue;
    }
  }

  @Override
  public Short getShort(String key) {
    String value = getString(key);
    return value == null ? null : Short.parseShort(value);
  }

  @Override
  public short getShort(String key, short defaultValue) {
    String value = getString(key);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Short.parseShort(value);
    } catch (Exception e) {
      logger.warn("fail to parse {} as short", value);
      return defaultValue;
    }
  }

  @Override
  public Float getFloat(String key) {
    String value = getString(key);
    return value == null ? null : Float.parseFloat(key);
  }

  @Override
  public float getFloat(String key, float defaultValue) {
    String value = getString(key);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Float.parseFloat(value);
    } catch (Exception e) {
      logger.warn("fail to parse {} as float", value);
      return defaultValue;
    }
  }

  @Override
  public Double getDouble(String key) {
    String value = getString(key);
    return value == null ? null : Double.parseDouble(key);
  }

  @Override
  public double getDouble(String key, double defaultValue) {
    String value = getString(key);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Double.parseDouble(value);
    } catch (Exception e) {
      logger.warn("fail to parse {} as double", value);
      return defaultValue;
    }
  }

  @Override
  public Long getLong(String key) {
    String value = getString(key);
    return value == null ? null : Long.parseLong(key);
  }

  @Override
  public long getLong(String key, long defaultValue) {
    String value = getString(key);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Long.parseLong(value);
    } catch (Exception e) {
      logger.warn("fail to parse {} as long", value);
      return defaultValue;
    }
  }

  @Override
  public int getInteger(String key, int defaultValue) {
    String value = getString(key);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      logger.warn("fail to parse {} as int", value);
      return defaultValue;
    }
  }

  @Override
  public Integer getInteger(String key) {
    String value = getString(key);
    return value == null ? null : Integer.parseInt(value);
  }

}
