package com.xzchaoo.learn.config.myconfig.businessconfig;

import com.xzchaoo.learn.config.myconfig.core.Config;

/**
 * @author zcxu
 * @date 2018/5/30 0030
 */
public class AbstractConfig implements RefreshableConfig {
  protected final Config config;

  public AbstractConfig(Config config) {
    this.config = config;
  }

  @Override
  public void refresh() {
    // empty
  }
}
