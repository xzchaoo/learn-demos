package com.xzchaoo.learn.config.myconfig.biz.parsers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class Parsers {
  public static final Parser<String> STRING = (clazz, x) -> x;
  public static final Parser<Integer> INTEGER = (clazz, x) -> Integer.parseInt(x);
  public static final Parser<Float> FLOAT = (clazz, x) -> Float.parseFloat(x);
  public static final Parser<Double> DOUBLE = (clazz, x) -> Double.parseDouble(x);
  // TODO 需要注意
  public static final Parser<Boolean> BOOLEAN = (clazz, x) -> Boolean.parseBoolean(x);
  public static final Parser<Long> LONG = (clazz, x) -> Long.parseLong(x);

  public static <T> Parser<List<T>> listParser(char separator, Class<T> valueType, Parser<T> valueParser) {
    return new CollectionParser<>(separator, valueType, valueParser, ArrayList::new);
  }

  public static <T> Parser<Set<T>> setParser(char separator, Class<T> valueType, Parser<T> valueParser) {
    return new CollectionParser<>(separator, valueType, valueParser, HashSet::new);
  }

  public static <T, C extends Collection<? super T>> Parser<C> collectionParser(char separator, Class<T> valueType,
                                                                                Parser<T> valueParser, Supplier<C> collectionSupplier) {
    return new CollectionParser<>(separator, valueType, valueParser, collectionSupplier);
  }

  public static <K, V> MapParser<K, V> mapParser(char separator, char separator2, Class<K> keyType, Parser<K> keyParser,
                                                 Class<V> valueType, Parser<V> valueParser) {
    return new MapParser<>(separator, separator2, keyType, keyParser, valueType, valueParser);
  }

}
