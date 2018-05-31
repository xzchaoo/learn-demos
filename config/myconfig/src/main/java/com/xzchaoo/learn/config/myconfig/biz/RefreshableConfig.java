package com.xzchaoo.learn.config.myconfig.biz;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public interface RefreshableConfig {
  /**
   * 配置变化的回调
   */
  void refresh();
}
