package com.xzchaoo.learn.config.myconfig.core.config.extra;

/**
 * 环境变量
 *
 * @author xzchaoo
 * @date 2018/5/25
 */
public class EnvironmentConfig extends UnmodifiableMapConfig {
  public static final EnvironmentConfig INSTANCE = new EnvironmentConfig();

  public EnvironmentConfig() {
    super("Environment", System.getenv());
  }
}
