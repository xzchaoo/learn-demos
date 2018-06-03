package com.xzchaoo.learn.config.myconfig.core;

/**
 * @author xzchaoo
 * @date 2018/6/2
 */
@FunctionalInterface
public interface ConfigChangeListener {
  /**
   * 当配置发生变时的回调
   *
   * @param config 发生变化的config
   */
  void onConfigChange(Config config);
}
