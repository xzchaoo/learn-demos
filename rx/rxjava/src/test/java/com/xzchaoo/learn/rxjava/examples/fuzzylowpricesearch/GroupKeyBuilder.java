package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

/**
 * 构建分组key
 *
 * @author zcxu
 * @date 2018/3/1 0001
 */
@FunctionalInterface
public interface GroupKeyBuilder {
  String buildKey(LowPriceCalendarData data);
}
