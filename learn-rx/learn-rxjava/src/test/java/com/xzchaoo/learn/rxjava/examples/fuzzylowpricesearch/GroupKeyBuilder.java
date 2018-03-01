package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch.LowPriceCalendarData;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
@FunctionalInterface
public interface GroupKeyBuilder {
  String buildKey(LowPriceCalendarData data);
}
