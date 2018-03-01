package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
@Getter
@Setter
@ToString
public class LowPriceCalendarData {
  private String datePairKey;

  private String departureCityCode;
  private String arrivalCityCode;

  private int value;

  //下面这些是经过解析后的数据
  private LocalDate outboundDate;
  private LocalDate inboundDate;
  private int salesPrice;
  private int totalPrice;
  private int nonStopSalesPrice;
  private int nonStopTotalPrice;
  private long timestamp;
}
