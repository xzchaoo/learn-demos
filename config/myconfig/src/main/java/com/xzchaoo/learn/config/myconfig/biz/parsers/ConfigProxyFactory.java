package com.xzchaoo.learn.config.myconfig.biz.parsers;

import com.xzchaoo.learn.config.myconfig.core.Config;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class ConfigProxyFactory {
  public static ConfigProxy from(Config config) {
    return new ConfigProxyImpl(config);
  }
}
