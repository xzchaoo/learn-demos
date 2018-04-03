package com.xzchaoo.learn.serialization.jackson.protobuf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchRequest {
  @JsonProperty(index = 1)
  private RequestHeader requestHeader;
  @JsonProperty(index = 2)
  private SearchCriteria searchCriteria;
}
