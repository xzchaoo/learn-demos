package com.xzchaoo.learn.config.myconfig.core;

/**
 * @author xzchaoo
 * @date 2018/5/25
 */
@FunctionalInterface
public interface ConfigObserver {
  /**
   * 当配置发生变化时
   *
   * @param config 发生变化的config
   */
  void onChange(Config config);
}
