package com.xzchaoo.learn.config.myconfig.businessconfig;

import com.xzchaoo.learn.config.myconfig.config.CompositeConfig;
import com.xzchaoo.learn.config.myconfig.core.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zcxu
 * @date 2018/5/30
 */
public class ConfigManager {
  private static final FooConfig FOO_CONFIG = new FooConfig();
  
  public static void init() {
    List<Config> childConfigs = new ArrayList<>();
    Config compositeConfig = new CompositeConfig(childConfigs);
    compositeConfig.subscribe((config) -> {

    });
  }
}
