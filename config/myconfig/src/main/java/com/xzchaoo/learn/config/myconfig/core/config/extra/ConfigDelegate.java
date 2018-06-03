package com.xzchaoo.learn.config.myconfig.core.config.extra;

import com.google.common.base.Preconditions;
import com.xzchaoo.learn.config.myconfig.core.Config;
import com.xzchaoo.learn.config.myconfig.core.config.AbstractConfig;

import java.util.Map;

/**
 * @author xzchaoo
 * @date 2018/6/2
 */
public class ConfigDelegate extends AbstractConfig {
  private volatile Config delegate;

  public ConfigDelegate(String name, Config delegate) {
    super(name);
    Preconditions.checkNotNull(delegate);
    this.delegate = delegate;
  }

  @Override
  public String getString(String key) {
    return delegate.getString(key);
  }

  @Override
  public String getString(String key, String defaultValue) {
    return delegate.getString(key, defaultValue);
  }

  @Override
  public boolean contains(String key) {
    return delegate.contains(key);
  }

  @Override
  public Map<String, String> asMap() {
    return delegate.asMap();
  }

  /**
   * 替换配置
   *
   * @param config
   */
  @SuppressWarnings("unused")
  public void replaceConfig(Config config) {
    this.delegate = config;
    super.notifyChanged();
  }

  @Override
  public String toString() {
    return delegate.toString();
  }
}
