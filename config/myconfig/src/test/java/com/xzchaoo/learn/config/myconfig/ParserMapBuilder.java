package com.xzchaoo.learn.config.myconfig;

import com.xzchaoo.learn.config.myconfig.configs.CollectionParserImpl;
import com.xzchaoo.learn.config.myconfig.configs.MapParserImpl2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class ParserMapBuilder {
  private Map<String, Function<String, ?>> parserMap = new HashMap<>();

  public static ParserMapBuilder create() {
    return new ParserMapBuilder();
  }

  public ParserMapBuilder registerList(String key, char separator, Function<String, ?> valueParser) {
    parserMap.put(key, new CollectionParserImpl<>(separator, valueParser, ArrayList::new));
    return this;
  }

  public ParserMapBuilder registerSet(String key, char separator, Function<String, ?> valueParser) {
    parserMap.put(key, new CollectionParserImpl<>(separator, valueParser, HashSet::new));
    return this;
  }

  public <T> ParserMapBuilder registerMap(String key, char separator, Function<String, T> valueParser, Supplier<?
    extends Collection<? super T>> collectionSupplier) {
    parserMap.put(key, new CollectionParserImpl<>(separator, valueParser, collectionSupplier));
    return this;
  }

  public <K, V> ParserMapBuilder registerMap(String key, char separator, char separator2, Function<String, ? extends K> keyParser,
                                              Function<String, ? extends V> valueParser) {
    parserMap.put(key, new MapParserImpl2<>(separator, separator2, keyParser, valueParser));
    return this;
  }

  public Map<String, Function<String, ?>> build() {
    return Collections.unmodifiableMap(parserMap);
  }
}
