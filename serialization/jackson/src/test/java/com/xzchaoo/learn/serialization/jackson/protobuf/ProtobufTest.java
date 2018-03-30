package com.xzchaoo.learn.serialization.jackson.protobuf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufFactory;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;
import com.xzchaoo.learn.serialization.jackson.protobuf.dto.RequestHeader;
import com.xzchaoo.learn.serialization.jackson.protobuf.dto.SearchCriteria;
import com.xzchaoo.learn.serialization.jackson.protobuf.dto.SearchRequest;

import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ProtobufTest {
  @Test
  public void test() throws Exception {
    ObjectMapper om = new ObjectMapper(new ProtobufFactory());
    om.findAndRegisterModules();
    om.registerModule(new XzcSimpleModule());


    ProtobufSchemaGenerator gen = new ProtobufSchemaGenerator();
    om.acceptJsonFormatVisitor(SearchRequest.class, gen);
    //命名空间不够大 导致后者的定义覆盖了前者
    om.acceptJsonFormatVisitor(com.xzchaoo.learn.serialization.jackson.protobuf.dto2.SearchRequest.class, gen);
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

    byte[] bytes = om.writerFor(SearchRequest.class).with(schemaWrapper).writeValueAsBytes(req);
    //calendar序列化会丢失时区信息
    SearchRequest req2 = om.readerFor(SearchRequest.class).with(schemaWrapper).readValue(bytes);
    System.out.println(req);
    System.out.println(req2);
    System.out.println(req.getSearchCriteria().getAcalendar().equals(req2.getSearchCriteria().getAcalendar()));
    System.out.println(Arrays.toString(req.getSearchCriteria().getIntArray()));
    System.out.println(Arrays.toString(req2.getSearchCriteria().getIntArray()));
  }
}
