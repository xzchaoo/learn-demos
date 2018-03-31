package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class XzcLocalDateSerializer extends JsonSerializer<XzcLocalDate> {
  @Override
  public void serialize(XzcLocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (value == null) {
      gen.writeNull();
    } else {
      gen.writeNumber(value.getYear() * 10000 + value.getMonth() * 100 + value.getDay());
    }
  }
}
