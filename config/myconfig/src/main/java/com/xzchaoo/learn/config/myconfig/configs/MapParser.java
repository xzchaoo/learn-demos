package com.xzchaoo.learn.config.myconfig.configs;

import java.util.Map;

/**
 * @author xzchaoo
 * @date 2018/5/30
 */
public interface MapParser<K, V> {
  Map<K, V> parse(String str);
}
