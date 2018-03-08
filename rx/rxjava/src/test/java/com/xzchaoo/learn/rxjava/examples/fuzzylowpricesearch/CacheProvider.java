package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import java.util.Map;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
public interface CacheProvider {
  Map<String, String> hgetall(String key);

  Map<String, String> hmget(String key, String[] fields);
}
