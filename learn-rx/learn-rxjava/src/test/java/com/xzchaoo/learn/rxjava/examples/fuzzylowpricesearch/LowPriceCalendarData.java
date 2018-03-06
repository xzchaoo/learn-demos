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
  /**
   * 日期对的key, 对于OW则为 yyyy/MM/dd 格式 对于RT则为 yyyy/MM/dd-yyyy/MM/dd 格式
   */
  private String datePairKey;
  private String departureCityCode;
  private String arrivalCityCode;

  //4种价格, TODO 其实存的时候是有分 价格和税的 是否要再建立一个结构体?

  //保证>0
  private int salesPrice;
  private int totalPrice;

  private int nonStopSalesPrice;//如果不存在则为-1
  private int nonStopTotalPrice;//如果不存在则为-1

  private long timestamp;
  //下面这些是经过解析后的数据
  private LocalDate outboundDate;
  private LocalDate inboundDate;
}

