package com.xzchaoo.learn.config.myconfig.configs;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.function.Function;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class MapParserImpl<K, V> implements MapParser<K, V, Map<K, V>> {
  private final char separator;
  private final Function<String, ? extends Pair<? extends K, ? extends V>> pairParser;

  public MapParserImpl(char separator, Function<String, ? extends Pair<? extends K, ? extends V>> pairParser) {
    this.separator = separator;
    this.pairParser = pairParser;
  }

  @Override
  public Map<K, V> apply(String str) {
    return ConfigParseUtils.parseMap(str, separator, pairParser);
  }
}
