package com.xzchaoo.learn.config.myconfig.core.property;

import com.xzchaoo.learn.config.myconfig.core.config.MapConfig;
import com.xzchaoo.learn.config.myconfig.core.parser.Parser;
import com.xzchaoo.learn.config.myconfig.core.parser.ParserProvider;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xzchaoo
 * @date 2018/6/3
 */
public class TestConfig extends MapConfig implements PropertySource {
  private final ParserProvider parserProvider;

  private final ConcurrentHashMap<PropertyKey<?>, PropertyContainer<?>> cache = new ConcurrentHashMap<>();

  public TestConfig(ParserProvider parserProvider) {
    this.parserProvider = parserProvider;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> PropertyContainer<T> getProperty(String key, Class<T> clazz, T defaultValue) {
    PropertyKey<T> propertyKey = new PropertyKey<>(key, clazz);
    return (PropertyContainer<T>) cache.computeIfAbsent(propertyKey, ignore -> {
      Parser<T> parser = parserProvider.getParser(clazz);
      return new PropertyContainerImpl<T>(key, this, parser, defaultValue);
    });
  }
}
