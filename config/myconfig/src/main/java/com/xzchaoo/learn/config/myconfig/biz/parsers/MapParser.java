package com.xzchaoo.learn.config.myconfig.biz.parsers;

import com.xzchaoo.learn.config.myconfig.configs.ConfigParseUtils;

import java.util.Map;
import java.util.function.Function;

/**
 * @author xzchaoo
 * @date 2018/6/1
 */
public class MapParser<K, V> implements Parser<Map<K, V>> {
  private final char separator;
  private final char separator2;
  private final Function<String, ? extends K> keyParseFunction;
  private final Function<String, ? extends V> valueParseFunction;

  public MapParser(char separator, char separator2, Class<K> keyType, Parser<K> keyParser, Class<V> valueType,
                   Parser<V> valueParser) {
    this.separator = separator;
    this.separator2 = separator2;
    this.keyParseFunction = new ParserFunction<>(keyType, keyParser);
    this.valueParseFunction = new ParserFunction<>(valueType, valueParser);
  }

  @Override
  public Map<K, V> parse(Class<Map<K, V>> clazz, String str) {
    return ConfigParseUtils.parseMap(str, separator, separator2, keyParseFunction, valueParseFunction);
  }
}
