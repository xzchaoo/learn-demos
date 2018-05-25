package com.xzchaoo.learn.config.myconfig.configs;

import java.util.Map;

/**
 * 环境变量
 *
 * @author xzchaoo
 * @date 2018/5/25
 */
public class EnvConfig extends MapConfig {
  public EnvConfig() {
    super(System.getenv());
  }

  @Override
  public void replace(Map<String, String> map) {
    throw new UnsupportedOperationException();
  }
}
