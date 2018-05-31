package com.xzchaoo.learn.config.myconfig.configs;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * @author xzchaoo
 * @date 2018/5/30
 */
public class ConfigParseUtils {
  public static <T> List<T> parseList(String str, char separator, Function<String, ? extends T> parser) {
    return parseCollection(str, separator, parser, ArrayList::new);
  }

  public static <T> Set<T> parseSet(String str, char separator, Function<String, ? extends T> valueParser) {
    return parseCollection(str, separator, valueParser, HashSet::new);
  }

  public static <T, C extends Collection<? super T>> C parseCollection(
    String str, char separator, Function<String, ? extends T> valueParser, IntFunction<C> collectionSupplier) {
    if (StringUtils.isEmpty(str)) {
      return collectionSupplier.apply(0);
    }
    // null empty check

    String[] ss = StringUtils.split(str, separator);
    C result = collectionSupplier.apply(ss.length);
    for (String s : ss) {
      // 返回null则忽略
      T value = valueParser.apply(s);
      if (value != null) {
        result.add(value);
      }
    }
    return result;
  }

  public static <K, V> Map<K, V> parseMap(String str, char separator, Function<String, ? extends Pair<? extends K, ? extends V>> pairParser) {
    if (StringUtils.isEmpty(str)) {
      return new HashMap<>();
    }
    // null empty check
    String[] ss = StringUtils.split(str, separator);
    Map<K, V> map = new HashMap<>();
    for (String s : ss) {
      Pair<? extends K, ? extends V> pair = pairParser.apply(s);
      if (pair != null) {
        map.put(pair.getKey(), pair.getValue());
      }
    }
    return map;
  }

  public static <K, V> Map<K, V> parseMap(
    String str, char separator, char separator2,
    Function<String, ? extends K> keyParser, Function<String, ? extends V> valueParser) {
    if (StringUtils.isEmpty(str)) {
      return new HashMap<>();
    }
    // null empty check
    String[] ss = StringUtils.split(str, separator);
    Map<K, V> map = new HashMap<>();
    for (String s : ss) {
      String[] keyAndValue = StringUtils.split(s, separator2);
      // 只指定了 "key" 或 "key=" 没有指定value 则会导致结果个数为1
      int n = keyAndValue.length;
      if (n == 2) {
        K key = keyParser.apply(keyAndValue[0]);
        V value = valueParser.apply(keyAndValue[1]);
        map.put(key, value);
      } else {
        throw new IllegalArgumentException("Unable to parse string to map " + str);
      }
    }
    return map;
  }

  public static <K, V> Map<K, List<V>> parseMap(
    String str, char separator, char separator2, char separator3,
    Function<String, ? extends K> keyParser, Function<String, ? extends V> valueParser) {
    if (StringUtils.isEmpty(str)) {
      return new HashMap<>();
    }
    // null empty check
    String[] ss = StringUtils.split(str, separator);
    // TODO 预先计算map大小
    Map<K, List<V>> map = new HashMap<>();
    for (String s : ss) {
      String[] keyAndValue = StringUtils.split(s, separator2);
      // 只指定了 "key" 或 "key=" 没有指定value 则会导致结果个数为1
      int n = keyAndValue.length;
      if (n == 2) {
        K key = keyParser.apply(keyAndValue[0]);
        List<V> value = parseList(keyAndValue[1], separator3, valueParser);
        map.put(key, value);
      } else {
        throw new IllegalArgumentException("Unable to parse string to map " + str);
      }
    }
    return map;
  }
}
