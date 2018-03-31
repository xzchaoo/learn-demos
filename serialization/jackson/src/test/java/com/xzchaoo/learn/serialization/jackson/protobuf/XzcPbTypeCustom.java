package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class XzcPbTypeCustom {
  private DataType dataType;
  private JsonSerializer<?> serializer;
  private JsonDeserializer<?> deserializer;
}
