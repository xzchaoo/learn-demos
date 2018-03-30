package com.xzchaoo.learn.serialization.jackson.protobuf.dto2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xzchaoo.learn.serialization.jackson.protobuf.dto.RequestHeader;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchRequest {
  @JsonProperty(index = 1)
  private RequestHeader requestHeader;
}
