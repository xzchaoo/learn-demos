package com.xzchaoo.learn.config.myconfig.core;

import java.util.Map;

/**
 * TODO 其他基本数据类型
 * 用于描述一份 properties 配置
 *
 * @author xzchaoo
 * @date 2018/5/25
 */
@SuppressWarnings("unused")
public interface Config {
  /**
   * 获取该配置的名称
   *
   * @return
   */
  String getName();

  /**
   * 获取一个字符串
   *
   * @param key key
   * @return key对应的value, 可能为null
   */
  String getString(String key);

  /**
   * 获取一个字符串, 如果不存在则返回提供的默认值
   *
   * @param key          key
   * @param defaultValue 默认值
   * @return key对应的value, 如果为null则返回defaultValue
   */
  String getString(String key, String defaultValue);

  /**
   * 获取一个integer, 如果解析失败会抛出异常
   *
   * @param key
   * @return 返回Integer实例, 可能为null
   */
  Integer getInteger(String key);

  /**
   * 获取一个integer, 如果不存在或解析失败则返回提供的默认值
   *
   * @param key          key
   * @param defaultValue 默认值
   * @return int
   */
  int getInteger(String key, int defaultValue);

  /**
   * 获取一个boolean, 如果解析失败会抛出异常
   *
   * @param key key
   * @return boolean, 可能为null
   */
  Boolean getBoolean(String key);

  /**
   * 获取一个boolean, 如果不存在或解析失败则返回提供的默认值
   *
   * @param key          key
   * @param defaultValue 默认值
   * @return boolean
   */
  boolean getBoolean(String key, boolean defaultValue);

  /**
   * 获取一个short, 如果解析失败会抛出异常
   *
   * @param key key
   * @return short, 可能为null
   */
  Short getShort(String key);

  /**
   * 获取一个short, 如果不存在或解析失败则返回提供的默认值
   *
   * @param key          key
   * @param defaultValue 默认值
   * @return short
   */
  short getShort(String key, short defaultValue);

  /**
   * 获取一个float, 如果解析失败会抛出异常
   *
   * @param key key
   * @return float, 可能为null
   */
  Float getFloat(String key);

  /**
   * 获取一个float, 如果不存在或解析失败则返回提供的默认值
   *
   * @param key          key
   * @param defaultValue 默认值
   * @return float
   */
  float getFloat(String key, float defaultValue);

  /**
   * 获取一个double, 如果解析失败会抛出异常
   *
   * @param key key
   * @return double, 可能为null
   */
  Double getDouble(String key);

  /**
   * 获取一个double, 如果不存在或解析失败则返回提供的默认值
   *
   * @param key          key
   * @param defaultValue 默认值
   * @return double
   */
  double getDouble(String key, double defaultValue);

  /**
   * 获取一个long, 如果解析失败会抛出异常
   *
   * @param key key
   * @return long, 可能为null
   */
  Long getLong(String key);

  /**
   * 获取一个long, 如果不存在或解析失败则返回提供的默认值
   *
   * @param key          key
   * @param defaultValue 默认值
   * @return long
   */
  long getLong(String key, long defaultValue);


  /**
   * 判断是否包含某个key
   *
   * @param key
   * @return boolean
   */
  boolean contains(String key);

  /**
   * 返回当前配置的map形式
   *
   * @return 不可变的map对象, 非null
   */
  Map<String, String> asMap();

  /**
   * 监听该配置的变化
   *
   * @param listener 监听器
   */
  void addListener(ConfigChangeListener listener);

  /**
   * 移除监听器
   *
   * @param listener 监听器
   */
  void removeListener(ConfigChangeListener listener);
}
