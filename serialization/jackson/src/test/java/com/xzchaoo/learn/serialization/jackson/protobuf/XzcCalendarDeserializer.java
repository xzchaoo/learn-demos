package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Calendar;

public class XzcCalendarDeserializer extends JsonDeserializer<Calendar> {
  @Override
  public Calendar deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
    LongWrapper longWrapper = p.readValueAs(LongWrapper.class);
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(longWrapper.getValue());
    return c;
  }
}
