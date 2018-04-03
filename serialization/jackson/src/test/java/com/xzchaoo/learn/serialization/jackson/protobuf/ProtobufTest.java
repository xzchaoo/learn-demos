package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufFactory;
import com.fasterxml.jackson.dataformat.protobuf.protoparser.protoparser.DataType;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.xzchaoo.learn.serialization.jackson.protobuf.dto.RequestHeader;
import com.xzchaoo.learn.serialization.jackson.protobuf.dto.SearchCriteria;
import com.xzchaoo.learn.serialization.jackson.protobuf.dto.SearchRequest;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class ProtobufTest {
  @Test
  public void test() throws Exception {
    ObjectMapper om = new ObjectMapper(new ProtobufFactory());
    om.findAndRegisterModules();
    om.registerModule(new XzcSimpleModule());

    XzcCustom xzcCustom = new XzcCustom();
    XzcPbTypeCustom xzcLocalDateCustom = new XzcPbTypeCustom();
    xzcLocalDateCustom.setDataType(DataType.ScalarType.INT32);
    xzcCustom.getMap().put(om.getTypeFactory().constructType(XzcLocalDate.class), xzcLocalDateCustom);
    SimpleModule custom = new SimpleModule();
    custom.addSerializer(XzcLocalDate.class, new XzcLocalDateSerializer());
    custom.addDeserializer(XzcLocalDate.class, new XzcLocalDateDeserializer());
    om.registerModule(custom);
    XzcPbTypeCustom calendarCustom = new XzcPbTypeCustom();
    calendarCustom.setDataType(DataType.NamedType.create("LongWrapper"));
    xzcCustom.getMap().put(om.getTypeFactory().constructType(Calendar.class), calendarCustom);
    XzcPbTypeCustom jdk8LocalDateCustom = new XzcPbTypeCustom();
    jdk8LocalDateCustom.setDataType(DataType.NamedType.create("Jdk8LocalDate"));
    xzcCustom.getMap().put(om.getTypeFactory().constructType(LocalDate.class), jdk8LocalDateCustom);

    XzcProtobufSchemaGenerator gen = new XzcProtobufSchemaGenerator(xzcCustom);
    om.acceptJsonFormatVisitor(SearchRequest.class, gen);
    //命名空间不够大 导致后者的定义覆盖了前者
    //om.acceptJsonFormatVisitor(com.xzchaoo.learn.serialization.jackson.protobuf.dto2.SearchRequest.class, gen);
    ProtobufSchema schemaWrapper = gen.getGeneratedSchema();
    System.out.println(schemaWrapper.toString());

    SearchRequest req = new SearchRequest();
    req.setRequestHeader(new RequestHeader());
    SearchCriteria searchCriteria = new SearchCriteria();
    req.setSearchCriteria(searchCriteria);
    searchCriteria.setAint(1);
    searchCriteria.setAlong(2);
    searchCriteria.setAfloat(3.0f);
    searchCriteria.setAdouble(4.0d);
    searchCriteria.setAstring("5s");
    searchCriteria.setAdate(new Date());
    searchCriteria.setAcalendar(Calendar.getInstance());
    searchCriteria.setIntArray(new int[]{1, 2, 3});
    searchCriteria.setXzcLocalDate(new XzcLocalDate(1, 2, 3));
    searchCriteria.setAboolean(true);
    searchCriteria.setAbyte((byte) 127);
    searchCriteria.setAshort((short) 277);
    searchCriteria.setBytes(new byte[]{1, 2, 3});
    searchCriteria.setShorts(new short[]{1, 2, 3, 4});
    searchCriteria.setLongArray(new long[0]);

    byte[] bytes = om.writerFor(SearchRequest.class).with(schemaWrapper).writeValueAsBytes(req);
    //calendar序列化会丢失时区信息
    SearchRequest req2 = om.readerFor(SearchRequest.class).with(schemaWrapper).readValue(bytes);
    System.out.println(req);
    System.out.println(req2);
    System.out.println(req.getSearchCriteria().getAcalendar().equals(req2.getSearchCriteria().getAcalendar()));
  }
}
