package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

import java.util.Calendar;

public class XzcSimpleModule extends SimpleModule {
  public XzcSimpleModule() {
    this.addSerializer(Calendar.class, new XzcCalendarSerializer());
    this.addDeserializer(Calendar.class, new XzcCalendarDeserializer());
    //this.addSerializer(XzcLocalDate.class, new NumberSerializers.IntegerSerializer(Integer.class));
  }
}
