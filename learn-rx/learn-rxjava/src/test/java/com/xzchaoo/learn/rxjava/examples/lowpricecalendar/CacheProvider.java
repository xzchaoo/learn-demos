package com.xzchaoo.learn.rxjava.examples.lowpricecalendar;

import java.util.Map;

/**
 * @author zcxu
 * @date 2018/2/11 0011
 */
public interface CacheProvider {
  Map<String, String> hgetall(String key);
}
