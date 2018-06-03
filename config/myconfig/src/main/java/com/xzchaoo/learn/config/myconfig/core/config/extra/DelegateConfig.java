package com.xzchaoo.learn.config.myconfig.core.config.extra;

import com.google.common.base.Preconditions;
import com.xzchaoo.learn.config.myconfig.core.Config;
import com.xzchaoo.learn.config.myconfig.core.config.AbstractConfig;

import java.util.Map;

/**
 * 将实际配置委托给另外一个Config
 *
 * @author xzchaoo
 * @date 2018/6/2
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DelegateConfig extends AbstractConfig {
  private volatile Config delegate;

  public DelegateConfig(String name, Config delegate) {
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
  public synchronized void replaceConfig(Config config) {
    this.delegate = config;
    super.notifyConfigChanged();
  }

  @Override
  public String toString() {
    return delegate.toString();
  }
}
