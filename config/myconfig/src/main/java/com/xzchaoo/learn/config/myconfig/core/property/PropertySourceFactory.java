package com.xzchaoo.learn.config.myconfig.core.property;

import com.xzchaoo.learn.config.myconfig.core.Config;
import com.xzchaoo.learn.config.myconfig.core.parser.DefaultParserProvider;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xzchaoo
 * @date 2018/6/3
 */
public class PropertySourceFactory {
  private static final ConcurrentHashMap<Config, PropertySource> CACHE = new ConcurrentHashMap<>();
  private static final DefaultParserProvider DEFAULT_PARSER_PROVIDER = new DefaultParserProvider();

  public static PropertySource get(Config config) {
    return CACHE.computeIfAbsent(config, PropertySourceFactory::create);
  }

  private static PropertySource create(Config config) {
    return new ConfigBasedPropertySource(config, DEFAULT_PARSER_PROVIDER);
  }
}
