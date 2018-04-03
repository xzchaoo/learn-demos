package com.xzchaoo.learn.serialization.jackson.custom.protobuf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufFactory;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;

import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

public class PBTest {
  @Test
  public void test1() throws IOException {

    ObjectMapper mapper = new ObjectMapper(new ProtobufFactory());

    mapper.registerModule(new SimpleModule() {
      @Override
      public void setupModule(SetupContext context) {
        context.addSerializers(new SimpleSerializers(Collections.singletonList(new MyBigDecimalSerializer())));
        //context.addDeserializers(new SimpleDeserializers(Collections.singletonMap(BigDecimal.class, new MyBigDecimalDeserializer())));
      }
    });
//		mapper.setSerializerFactory(mapper.getSerializerFactory().withAdditionalSerializers(new SimpleSerializers(Arrays.asList(new MyBigDecimalSerializer()))));
//		mapper.getDeserializationContext().getFactory().withAdditionalDeserializers(new SimpleDeserializers(Collections.singletonMap(BigDecimal.class, new MyBigDecimalDeserializer())));
    //mapper.getDeserializationContext().getFactory().withAdditionalDeserializers()
    //mapper.setSerializerFactory()

    mapper.findAndRegisterModules();

    ProtobufSchemaGenerator gen = new ProtobufSchemaGenerator();
    mapper.acceptJsonFormatVisitor(User1.class, gen);
    ProtobufSchema schemaWrapper = gen.getGeneratedSchema();
    System.out.println(schemaWrapper.toString());

//		NativeProtobufSchema nativeProtobufSchema = schemaWrapper.getSource();
//		String asProtofile = nativeProtobufSchema.toString();
//		System.out.println(asProtofile);
    User1 u1 = new User1();
    u1.setId(32);
    u1.setDate(new Date());
    Card card = new Card();
    card.setId(9999);
    card.setName("我是中文");
    u1.setCard(null);
    //bigDecimal 会被序列化成 double 也是醉了
    u1.setMoney(new BigDecimal("70862045.814753270862045777777777777777"));
    byte[] data = mapper.writerFor(User1.class).with(schemaWrapper).writeValueAsBytes(u1);

    u1 = mapper.readerFor(User1.class).with(schemaWrapper).readValue(data);
    System.out.println(u1.getId());
    //System.out.println(u1.getUsername());
    System.out.println(u1.getMoney());
    //System.out.println(u1.getCard());

//		Mypb.User1 user1 = Mypb.User1.parseFrom(data);
//		System.out.println(user1);
//		System.out.println(user1.hasCard());
//		System.out.println(user1.getCard().getName());
  }
}
