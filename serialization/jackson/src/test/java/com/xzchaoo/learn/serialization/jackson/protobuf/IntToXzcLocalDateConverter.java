package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.databind.util.StdConverter;

public class IntToXzcLocalDateConverter extends StdConverter<Integer, XzcLocalDate> {
  @Override
  public XzcLocalDate convert(Integer value) {
    if (value == null) {
      return null;
    }
    XzcLocalDate date = new XzcLocalDate();
    date.setDay(value % 100);
    date.setMonth(value / 100 % 100);
    date.setYear(value / 10000);
    return date;
  }
}
