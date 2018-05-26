package com.xzchaoo.learn.config.myconfig.configs;

import java.util.Map;

/**
 * 环境变量
 *
 * @author xzchaoo
 * @date 2018/5/25
 */
public class EnvironmentConfig extends MapConfig {
  public static final EnvironmentConfig INSTANCE = new EnvironmentConfig();

  public EnvironmentConfig() {
    super(System.getenv());
  }

  @Override
  public void replace(Map<String, String> map) {
    throw new UnsupportedOperationException();
  }
}
