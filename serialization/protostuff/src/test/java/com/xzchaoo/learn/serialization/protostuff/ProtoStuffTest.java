package com.xzchaoo.learn.serialization.protostuff;

import com.xzchaoo.learn.serialization.protostuff.model.RequestHeader;
import com.xzchaoo.learn.serialization.protostuff.model.SearchCriteria;
import com.xzchaoo.learn.serialization.protostuff.model.SearchRequest;
import com.xzhcaoo.learn.User;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtobufOutput;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.val;

import static java.util.Arrays.asList;

/**
 * @author zcxu
 * @date 2017/12/22
 */
public class ProtoStuffTest {
  @Test
  public void test2() throws Exception {
    val schema = RuntimeSchema.createFrom(SearchRequest.class);
    //ProtostuffIOUtil.mergeFrom("",schema.newMessage(),schema);
    val sr = new SearchRequest();
    sr.setRequestHeader(new RequestHeader());
    sr.setSearchCriterias(asList(new SearchCriteria(), null, new SearchCriteria()));
    //sr.setSearchCriterias(asList(null, null));
    LinkedBuffer buffer = LinkedBuffer.allocate();
    byte[] data = ProtostuffIOUtil.toByteArray(sr, schema, buffer);
    System.out.println(Base64.getEncoder().encodeToString(data));
    buffer.clear();
    SearchRequest sr2 = schema.newMessage();
    ProtostuffIOUtil.mergeFrom(data, sr2, schema);
    System.out.println(sr2);
    System.out.println(sr.getSearchCriterias().size());
  }

  @Test
  public void test_stream() throws Exception {
    // 支持json pb protostuff

    val schema = RuntimeSchema.createFrom(SearchRequest.class);
    //ProtostuffIOUtil.mergeFrom("",schema.newMessage(),schema);
    val sr = new SearchRequest();
    sr.setRequestHeader(new RequestHeader());
    sr.setSearchCriterias(asList(new SearchCriteria(), null, new SearchCriteria()));
    //sr.setSearchCriterias(asList(null, null));
    LinkedBuffer buffer = LinkedBuffer.allocate();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ProtostuffIOUtil.writeTo(baos, sr, schema, buffer);
    System.out.println(Base64.getEncoder().encodeToString(baos.toByteArray()));
    SearchRequest sr2 = schema.newMessage();
    ProtostuffIOUtil.mergeFrom(new ByteArrayInputStream(baos.toByteArray()), sr2, schema);
    System.out.println(sr2);
    System.out.println(sr.getSearchCriterias().size());
    buffer.clear();

    buffer = LinkedBuffer.allocate();
    System.out.println(Base64.getEncoder().encodeToString(ProtobufIOUtil.toByteArray(sr, schema, buffer)));
    buffer.clear();
  }

  @Test
  public void test() throws IOException {
    Schema<User> s = RuntimeSchema.getSchema(User.class);
    LinkedBuffer lb = LinkedBuffer.allocate();
    User u = new User();
    u.setId(1L);
    u.setUsername("haa");
    s.writeTo(new ProtobufOutput(lb), u);
    LinkedBuffer.writeTo(System.out, lb);
  }
}
