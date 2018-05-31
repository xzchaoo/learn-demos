package com.xzchaoo.learn.config.myconfig.configs;

import java.util.Map;
import java.util.function.Function;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class MapParserImpl2<K, V> implements MapParser<K, V, Map<K, V>> {
  private final char separator;
  private final char separator2;
  private final Function<String, ? extends K> keyParser;
  private final Function<String, ? extends V> valueParser;

  public MapParserImpl2(char separator, char separator2, Function<String, ? extends K> keyParser, Function<String, ? extends V> valueParser) {
    this.separator = separator;
    this.separator2 = separator2;
    this.keyParser = keyParser;
    this.valueParser = valueParser;
  }

  @Override
  public Map<K, V> apply(String str) {
    return ConfigParseUtils.parseMap(str, separator, separator2, keyParser, valueParser);
  }
}
