package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import java.time.LocalDate;

/**
 * TODO
 *
 * @author zcxu
 * @date 2018/3/1 0001
 */
@FunctionalInterface
public interface DatePairFilter {
  /**
   * 返回true则保留
   *
   * @param outboundDate
   * @param inboundDate
   * @return
   */
  boolean filter(LocalDate outboundDate, LocalDate inboundDate);
}
