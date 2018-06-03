package com.xzchaoo.learn.config.myconfig.core.property;

import com.xzchaoo.learn.config.myconfig.core.Config;
import com.xzchaoo.learn.config.myconfig.core.parser.Parser;
import com.xzchaoo.learn.config.myconfig.core.parser.ParserProvider;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xzchaoo
 * @date 2018/6/3
 */
public class ConfigBasedPropertySource implements PropertySource {
  private final Config config;
  private final ParserProvider parserProvider;
  private final ConcurrentHashMap<PropertyKey<?>, Property<?>> cache = new ConcurrentHashMap<>();


  public ConfigBasedPropertySource(Config config, ParserProvider parserProvider) {
    this.config = config;
    this.parserProvider = parserProvider;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> Property<T> getProperty(String key, Class<T> clazz, T defaultValue) {
    PropertyKey<T> propertyKey = new PropertyKey<>(key, clazz);
    return (Property<T>) cache.computeIfAbsent(propertyKey, ignore -> {
      Parser<T> parser = parserProvider.getParser(clazz);
      return new PropertyImpl<>(key, config, parser, defaultValue);
    });
  }
}
