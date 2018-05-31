package com.xzchaoo.learn.config.myconfig.config;

import com.xzchaoo.learn.config.myconfig.core.Config;
import com.xzchaoo.learn.config.myconfig.core.ConfigObserver;
import com.xzchaoo.learn.config.myconfig.core.Subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xzchaoo
 * @date 2018/5/25
 */
public abstract class AbstractConfig implements Config {
  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
  protected final String name;
  private static final AtomicInteger COUNTER = new AtomicInteger(0);
  private CopyOnWriteArrayList<ConfigObserver> listeners = new CopyOnWriteArrayList<>();

  public AbstractConfig() {
    this.name = getClass().getSimpleName() + "-" + COUNTER.incrementAndGet();
  }

  public AbstractConfig(String name) {
    if (name == null) {
      throw new NullPointerException();
    }
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getInt(String key, int defaultValue) {
    String value = getString(key);
    // exception
    return value == null ? defaultValue : Integer.parseInt(value);
  }

  @Override
  public Integer getInt(String key) {
    String value = getString(key);
    return value == null ? null : Integer.parseInt(value);
  }

  @Override
  public Subscription subscribe(ConfigObserver configObserver) {
    listeners.add(configObserver);
    return () -> listeners.remove(configObserver);
  }

  protected void triggerChange() {
    for (ConfigObserver listener : listeners) {
      try {
        listener.onChange(this);
      } catch (Exception e) {
        LOGGER.error("更新配置失败", e);
      }
    }
  }
}
