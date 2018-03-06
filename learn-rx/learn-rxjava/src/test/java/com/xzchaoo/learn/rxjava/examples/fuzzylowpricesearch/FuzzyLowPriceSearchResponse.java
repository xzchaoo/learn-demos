package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

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
@AllArgsConstructor
@NoArgsConstructor
public class FuzzyLowPriceSearchResponse {
  private Object responseHeader;
  private FuzzyLowPriceSearchResult searchResult;
  private Object errorResult;
}
