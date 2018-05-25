package com.xzchaoo.learn.config.myconfig.core;

import java.util.Map;

/**
 * @author xzchaoo
 * @date 2018/5/25
 */
@SuppressWarnings("unused")
public interface Config {
  String getName();

  String getString(String key);

  String getString(String key, String defaultValue);

  Integer getInt(String key);

  int getInt(String key, int defaultValue);

  Map<String, String> getAsMap();

  Subscription subscribe(ConfigObserver configObserver);
}
