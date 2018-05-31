package com.xzchaoo.learn.config.myconfig;

import java.util.Map;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public interface RefreshableConfigWithSourceConfig {
  void refresh(Map<String, String> configMap);
}
