package com.xzchaoo.learn.config.myconfig.configs;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class CollectionParserImpl<T, C extends Collection<? super T>> implements CollectionParser<T, C> {
  private final char separator;
  private final Function<String, ? extends T> valueParser;
  private final Supplier<C> collectionSupplier;

  public CollectionParserImpl(char separator, Function<String, ? extends T> valueParser, Supplier<C> collectionSupplier) {
    this.separator = separator;
    this.valueParser = valueParser;
    this.collectionSupplier = collectionSupplier;
  }

  @Override
  public C apply(String str) {
    return ConfigParseUtils.parseCollection(str, separator, valueParser, collectionSupplier);
  }
}
