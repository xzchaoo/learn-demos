package com.xzchaoo.learn.config.myconfig.core;

import java.util.List;

/**
 * @author xzchaoo
 * @date 2018/6/2
 */
public interface CompositeConfig extends Config {

  /**
   * 获取子配置
   *
   * @return 不可变对象, 非null
   */
  List<Config> getConfigs();

  /**
   * 替换配置
   *
   * @param configs
   */
  void replaceConfigs(List<Config> configs);
}
