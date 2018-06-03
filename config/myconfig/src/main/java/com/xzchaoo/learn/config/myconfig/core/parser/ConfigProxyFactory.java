package com.xzchaoo.learn.config.myconfig.core.parser;


import com.xzchaoo.learn.config.myconfig.core.Config;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class ConfigProxyFactory {

  /**
   * 以config为配置源构建 ConfigProxy
   *
   * @param config
   * @return
   */
  public static ConfigProxy from(Config config) {
    return from(config, new DefaultParserProvider(null));
  }

  public static ConfigProxy from(Config config, ParserProvider parserProvider) {
    return new ConfigProxyImpl(config, parserProvider);
  }
}
