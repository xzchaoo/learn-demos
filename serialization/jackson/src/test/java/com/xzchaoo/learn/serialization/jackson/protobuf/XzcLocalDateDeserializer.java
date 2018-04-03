package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class XzcLocalDateDeserializer extends JsonDeserializer<XzcLocalDate> {
  @Override
  public XzcLocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    int value;
    if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
      value = p.getIntValue();
    } else {
      value = 0;
    }
    XzcLocalDate date = new XzcLocalDate();
    date.setDay(value % 100);
    date.setMonth(value / 100 % 100);
    date.setYear(value / 10000);
    return date;
  }
}
