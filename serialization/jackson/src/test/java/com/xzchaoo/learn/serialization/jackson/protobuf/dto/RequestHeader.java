package com.xzchaoo.learn.serialization.jackson.protobuf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RequestHeader {
  @JsonProperty(index = 1)
  private String a;
}
