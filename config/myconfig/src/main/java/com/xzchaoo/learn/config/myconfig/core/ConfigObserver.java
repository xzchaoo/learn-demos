package com.xzchaoo.learn.config.myconfig.core;

import com.xzchaoo.learn.config.myconfig.core.Config;

/**
 * @author xzchaoo
 * @date 2018/5/25
 */
@FunctionalInterface
public interface ConfigObserver {
  /**
   * 当配置发生变化时
   *
   * @param config
   */
  void onChange(Config config);
}
