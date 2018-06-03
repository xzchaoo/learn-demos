package com.xzchaoo.learn.config.myconfig.core.parser;


import com.xzchaoo.learn.config.myconfig.core.Config;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
@SuppressWarnings("WeakerAccess")
public class ConfigProxyFactory {
  private static final DefaultParserProvider DEFAULT_PARSER_PROVIDER = new DefaultParserProvider();

  /**
   * 以config为配置源构建 ConfigProxy
   *
   * @param config
   * @return
   */
  public static ConfigProxy from(Config config) {
    return from(config, DEFAULT_PARSER_PROVIDER);
  }

  public static ConfigProxy from(Config config, ParserProvider parserProvider) {
    return new ConfigProxyImpl(config, parserProvider);
  }
}
