package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import java.util.List;

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
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FuzzyLowPriceSearchCriteria {
  private List<FuzzyLowPriceRequestSegment> segments;
  private PriceType priceType;
  private GroupMode groupMode;

  /*computed properties*/
  private TripType tripType;
}
