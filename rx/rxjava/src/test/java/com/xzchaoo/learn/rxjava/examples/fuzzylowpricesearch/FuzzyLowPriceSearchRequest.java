package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
@Getter
@Setter
public class FuzzyLowPriceSearchRequest {
  private Object requestHeader;
  private FuzzyLowPriceSearchCriteria searchCriteria;
}
