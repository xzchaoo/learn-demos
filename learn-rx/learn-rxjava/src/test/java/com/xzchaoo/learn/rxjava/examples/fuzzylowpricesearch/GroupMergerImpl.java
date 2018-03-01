package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import com.google.common.base.Preconditions;

import java.util.Comparator;
import java.util.Map;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
public class GroupMergerImpl implements GroupMerger {
  private final GroupKeyBuilder groupKeyBuilder;
  private final Comparator<LowPriceCalendarData> comparator;

  public GroupMergerImpl(GroupKeyBuilder groupKeyBuilder, Comparator<LowPriceCalendarData> comparator) {
    Preconditions.checkNotNull(groupKeyBuilder);
    Preconditions.checkNotNull(comparator);
    this.groupKeyBuilder = groupKeyBuilder;
    this.comparator = comparator;
  }

  @Override
  public void merge(Map<String, LowPriceCalendarData> calendar, Map<String, LowPriceCalendarData> groupResult) {
    for (Map.Entry<String, LowPriceCalendarData> e : calendar.entrySet()) {
      LowPriceCalendarData data = e.getValue();
      String groupKey = groupKeyBuilder.buildKey(data);
      LowPriceCalendarData oldData = groupResult.get(groupKey);
      if (oldData == null || comparator.compare(data, oldData) < 0) {
        groupResult.put(groupKey, data);
      }
    }
  }
}
