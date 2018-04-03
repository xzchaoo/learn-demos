package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.databind.util.StdConverter;

public class XzcLocalDateToIntConverter extends StdConverter<XzcLocalDate, Integer> {
  @Override
  public Integer convert(XzcLocalDate value) {
    if (value == null) {
      return null;
    }
    return value.getYear() * 10000 + value.getMonth() * 100 + value.getDay();
  }
}
