package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import java.io.IOException;
import java.util.Calendar;

public class XzcCalendarSerializer extends JsonSerializer {

  @Override
  public Class handledType() {
    return Calendar.class;
  }

  @Override
  public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType type) throws JsonMappingException {
//    JsonSerializer<Object> s = visitor.getProvider().findValueSerializer(LongWrapper.class);
//    s.acceptJsonFormatVisitor(visitor, visitor.getProvider().constructType(LongWrapper.class));
    visitor.getProvider().findValueSerializer(LongWrapper.class).acceptJsonFormatVisitor(visitor, type);
  }

  @Override
  public void serialize(Object value0, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    Calendar value = (Calendar) value0;
    //TODO null会进来吗?
    if (value == null) {
      gen.writeNull();
    } else {
      LongWrapper longWrapper = new LongWrapper();
      longWrapper.setValue(value.getTimeInMillis());
      serializers.findValueSerializer(LongWrapper.class).serialize(longWrapper, gen, serializers);
    }
  }
}
