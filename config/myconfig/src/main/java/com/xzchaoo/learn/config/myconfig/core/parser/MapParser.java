package com.xzchaoo.learn.config.myconfig.core.parser;


import com.xzchaoo.learn.config.myconfig.core.utils.ConfigParseUtils;

import java.util.Map;

/**
 * @author xzchaoo
 * @date 2018/6/1
 */
@SuppressWarnings("WeakerAccess")
public class MapParser<K, V> implements Parser<Map<K, V>> {
  private final char separator;
  private final char separator2;
  private final Parser<? extends K> keyParser;
  private final Parser<? extends V> valueParser;

  public MapParser(char separator, char separator2, Parser<? extends K> keyParser, Parser<? extends V> valueParser) {
    this.separator = separator;
    this.separator2 = separator2;
    this.keyParser = keyParser;
    this.valueParser = valueParser;
  }

  @Override
  public Map<K, V> parse(String str) {
    return ConfigParseUtils.parseMap(str, separator, separator2, keyParser, valueParser);
  }
}
