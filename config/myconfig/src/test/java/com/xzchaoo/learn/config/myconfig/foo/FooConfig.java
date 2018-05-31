package com.xzchaoo.learn.config.myconfig.foo;

import com.xzchaoo.learn.config.myconfig.IFooConfig;
import com.xzchaoo.learn.config.myconfig.ParserMapBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.concurrent.ThreadSafe;

/**
 * 不能lazy初始化, 否则会有加锁问题
 *
 * @author xzchaoo
 * @date 2018/5/31
 */
@SuppressWarnings("unchecked")
@ThreadSafe
public class FooConfig implements IFooConfig {
  private static final Logger LOGGER = LoggerFactory.getLogger(FooConfig.class);

  private volatile Map<String, String> rawConfig = Collections.emptyMap();
  private volatile Map<String, Object> cache = Collections.emptyMap();

  // 只能在初始化的时候访问
  private Map<String, Function<String, ?>> parserMap;

  public FooConfig() {
    // 注册过parser的 它们的值将会被替换成相应的结果 否则还是维持一个String在内存里
    parserMap = ParserMapBuilder.create()
      .registerList("key.list", ',', Integer::parseInt)
      .registerMap("key.map", '|', '=', Function.identity(), Function.identity())
      .build();
  }


  public synchronized void refresh(Map<String, String> newRawConfig) {
    Map<String, String> unmodifiableConfigMap = Collections.unmodifiableMap(new HashMap<>(newRawConfig));
    Map<String, String> modifiableConfigMap = new HashMap<>(newRawConfig);

    Map<String, Object> cache = new HashMap<>();
    for (Map.Entry<String, Function<String, ?>> e : parserMap.entrySet()) {
      // null check ?
      String key = e.getKey();
      String str = modifiableConfigMap.get(key);
      // TODO 解析失败?
      Object value = e.getValue().apply(str);
      cache.put(key, value);
      modifiableConfigMap.remove(key);
    }

    cache.putAll(modifiableConfigMap);

    // TODO 内存屏障
    this.rawConfig = unmodifiableConfigMap;
    this.cache = cache;
  }

  @Override
  public String getString(String key) {
    return (String) cache.get(key);
  }

  public String getString(String key, String defaultValue) {
    Object value = cache.get(key);
    return value == null ? (String) value : defaultValue;
  }

  @Override
  public Integer getInteger(String key) {
    Object value = cache.get(key);
    // exception
    return value == null ? null : Integer.parseInt((String) value);
  }

  @Override
  public int getInteger(String key, int defaultValue) {
    Object value = cache.get(key);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Integer.parseInt((String) value);
    } catch (NumberFormatException e) {
      LOGGER.warn("fail to parse {} to int", value, e);
      return defaultValue;
    }
  }

  @Override
  public Long getLong(String key) {
    Object value = cache.get(key);
    return value == null ? null : Long.parseLong((String) value);
  }

  @Override
  public long getLong(String key, long defaultValue) {
    Object value = cache.get(key);
    if (value == null) {
      return defaultValue;
    }
    try {
      return Long.parseLong((String) value);
    } catch (NumberFormatException e) {
      LOGGER.warn("fail to parse {} to long", value, e);
      return defaultValue;
    }
  }

  @Override
  public <T> List<T> getList(String key) {
    return getList(key, null);
  }

  @Override
  public <K, V> Map<K, V> getMap(String key) {
    return getMap(key, null);
  }

  @Override
  public <T> List<T> getList(String key, Supplier<? extends List<T>> defaultValueProvider) {
    return getOrDefault(key, defaultValueProvider);
  }

  @Override
  public <K, V> Map<K, V> getMap(String key, Supplier<? extends Map<K, V>> defaultValueProvider) {
    return getOrDefault(key, defaultValueProvider);
  }


  /**
   * 这个方法太危险 不要暴露出去
   *
   * @param key      key
   * @param supplier 默认值提供器
   * @param <T>      泛型
   * @return 泛型结果, 如果类型不正确的话会导致类型转换异常
   */
  private <T> T getOrDefault(String key, Supplier<? extends T> supplier) {
    T value = (T) cache.get(key);
    if (value == null && supplier != null) {
      value = supplier.get();
    }
    return value;
  }
}
