package com.xzchaoo.learn.config.myconfig.biz.parsers;

import com.xzchaoo.learn.config.myconfig.configs.ConfigParseUtils;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author xzchaoo
 * @date 2018/6/1
 */
public class CollectionParser<T, C extends Collection<? super T>> implements Parser<C> {
  private final char separator;
  private final Class<T> valueType;
  private final Parser<T> parser;
  private final Supplier<C> collectionSupplier;

  public CollectionParser(char separator, Class<T> valueType, Parser<T> parser, Supplier<C> collectionSupplier) {
    this.separator = separator;
    this.valueType = valueType;
    this.parser = parser;
    this.collectionSupplier = collectionSupplier;
  }

  @Override
  public C parse(Class<C> clazz, String str) {
    return ConfigParseUtils.parseCollection(str, separator, (sub) -> parser.parse(valueType, sub), collectionSupplier);
  }
}
