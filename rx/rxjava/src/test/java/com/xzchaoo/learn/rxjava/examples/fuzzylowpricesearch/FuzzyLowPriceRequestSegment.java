package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FuzzyLowPriceRequestSegment {
  private Set<String> departureCityCodes;
  private Set<String> arrivalCityCodes;
  private DateRange dateRange;

  /**
   * 仅对于RT和MT的第2段有效
   */
  private IntRange dayIntervalRange;
}
