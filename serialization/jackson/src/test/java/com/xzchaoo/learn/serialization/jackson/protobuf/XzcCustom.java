package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.databind.JavaType;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public class XzcCustom {
  @Getter
  private Map<JavaType, XzcPbTypeCustom> map = new HashMap<>();

  public XzcPbTypeCustom getCustom(JavaType javaType) {
    return map.get(javaType);
  }

}
