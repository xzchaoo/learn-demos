package com.xzchaoo.learn.config.myconfig.core.utils;

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
import java.util.function.Supplier;

/**
 * 将配置的字符串类型解析为集合和Map类型的辅助工具
 *
 * @author xzchaoo
 * @date 2018/5/30
 */
public class ConfigParseUtils {
  public static List<String> parseList(String str, char separator) {
    return parseList(str, separator, Function.identity());
  }

  public static <T> List<T> parseList(String str, char separator, Function<String, ? extends T> parser) {
    return parseCollection(str, separator, parser, ArrayList::new);
  }

  public static Set<String> parseSet(String str, char separator) {
    return parseSet(str, separator, Function.identity());
  }

  public static <T> Set<T> parseSet(String str, char separator, Function<String, ? extends T> valueParser) {
    return parseCollection(str, separator, valueParser, HashSet::new);
  }

  public static <T, C extends Collection<? super T>> C parseCollection(String str, char separator, Function<String, ? extends T> valueParser, Supplier<C> collectionSupplier) {
    if (StringUtils.isEmpty(str)) {
      return collectionSupplier.get();
    }

    String[] ss = StringUtils.split(str, separator);
    C result = collectionSupplier.get();
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
      String[] keyAndValue = StringUtils.split(s.trim(), separator2);
      // 只指定了 "key" 或 "key=" 没有指定value 则会导致结果个数为1
      int n = keyAndValue.length;
      if (n == 2) {
        K key = keyParser.apply(keyAndValue[0].trim());
        V value = valueParser.apply(keyAndValue[1].trim());
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
    String[] ss = StringUtils.split(str, separator);
    Map<K, List<V>> map = new HashMap<>();
    for (String s : ss) {
      String[] keyAndValueStr = StringUtils.split(s, separator2);
      // 只指定了 "key" 或 "key=" 没有指定value 则会导致结果个数为1, 比如 "a="
      int n = keyAndValueStr.length;
      if (n == 2) {
        K key = keyParser.apply(keyAndValueStr[0]);
        List<V> value = parseList(keyAndValueStr[1], separator3, valueParser);
        map.put(key, value);
      } else {
        throw new IllegalArgumentException("Unable to parse string to map " + str);
      }
    }
    return map;
  }
}
