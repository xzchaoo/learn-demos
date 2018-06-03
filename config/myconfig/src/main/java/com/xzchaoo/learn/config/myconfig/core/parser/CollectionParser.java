package com.xzchaoo.learn.config.myconfig.core.parser;


import com.xzchaoo.learn.config.myconfig.core.utils.ConfigParseUtils;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * @author xzchaoo
 * @date 2018/6/1
 */
@SuppressWarnings("WeakerAccess")
public class CollectionParser<T, C extends Collection<? super T>> implements Parser<C> {
  private final char separator;
  private final Parser<? extends T> parser;
  private final Supplier<C> collectionSupplier;

  public CollectionParser(char separator, Parser<? extends T> parser, Supplier<C> collectionSupplier) {
    this.separator = separator;
    this.parser = parser;
    this.collectionSupplier = collectionSupplier;
  }

  @Override
  public C parse(String str) {
    return ConfigParseUtils.parseCollection(str, separator, parser, collectionSupplier);
  }
}
