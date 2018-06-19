package com.xzchaoo.learn.serialization.protostuff.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xzchaoo
 * @date 2018/6/15
 */
@Getter
@Setter
@ToString
public class SearchRequest {
  private RequestHeader requestHeader;
  private List<SearchCriteria> searchCriterias;
}
