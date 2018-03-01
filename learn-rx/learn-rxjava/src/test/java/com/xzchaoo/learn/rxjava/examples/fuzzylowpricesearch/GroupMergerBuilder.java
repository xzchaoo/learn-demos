package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import com.google.common.base.Preconditions;

import java.util.Comparator;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
public class GroupMergerBuilder {
  public static final GroupKeyBuilder GROUP_BY_CITYPAIR = (data) -> data.getDepartureCityCode() + "-" + data.getArrivalCityCode();
  public static final GroupKeyBuilder GROUP_BY_DATEPAIR = LowPriceCalendarData::getDatePairKey;
  public static final Comparator<LowPriceCalendarData> COMPARED_BY_SALESPRICE = Comparator.comparingInt(LowPriceCalendarData::getSalesPrice);
  public static final Comparator<LowPriceCalendarData> COMPARED_BY_TOTALPRICE = Comparator.comparingInt(LowPriceCalendarData::getTotalPrice);

  private GroupKeyBuilder groupKeyBuilder;
  private Comparator<LowPriceCalendarData> comparator = COMPARED_BY_TOTALPRICE;

  public static GroupMergerBuilder create() {
    return new GroupMergerBuilder();
  }

  public GroupMergerBuilder groupBy(GroupKeyBuilder groupKeyBuilder) {
    Preconditions.checkNotNull(groupKeyBuilder);
    this.groupKeyBuilder = groupKeyBuilder;
    return this;
  }

  public GroupMergerBuilder sort(Comparator<LowPriceCalendarData> comparator) {
    Preconditions.checkNotNull(comparator);
    this.comparator = comparator;
    return this;
  }

  public GroupMerger build() {
    return new GroupMergerImpl(groupKeyBuilder, comparator);
  }
}
