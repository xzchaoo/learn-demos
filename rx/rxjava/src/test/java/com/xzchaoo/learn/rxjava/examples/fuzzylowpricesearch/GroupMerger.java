package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import java.util.Map;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
@FunctionalInterface
public interface GroupMerger {
  void merge(Map<String, LowPriceCalendarData> calendar, Map<String, LowPriceCalendarData> groupResult);
}
