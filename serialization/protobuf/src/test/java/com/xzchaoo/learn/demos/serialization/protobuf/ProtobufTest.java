package com.xzchaoo.learn.demos.serialization.protobuf;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schema.NativeProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;
import com.xzhcaoo.learn.LocalDateDeserializer;
import com.xzhcaoo.learn.LocalDateSerializer;
import com.xzhcaoo.learn.User;
import com.xzhcaoo.learn.User2;
import com.xzhcaoo.learn.UserProtos;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;

/**
 * created by xzchaoo at 2017/11/21
 *
 * @author xzchaoo
 */
public class ProtobufTest {
	@Test
	public void test() throws IOException {
		UserProtos.User.Builder b = UserProtos.User.newBuilder()
			.setId(233)
			.setUsername("hah")
			.setBirthday(
				UserProtos.LocalDateWrapper.newBuilder()
					.setYear(2017)
					.setMonth(1)
					.setDay(2)
			);

		UserProtos.User u = b.build();
		System.out.println(u);

		ProtobufMapper pm = new ProtobufMapper();
		pm.registerModule(new SimpleModule() {
			@Override
			public void setupModule(SetupContext context) {
//				SimpleSerializers sss = new SimpleSerializers();
//				sss.addSerializer(LocalDate.class, new LocalDateSerializer());
//				context.addSerializers(sss);
//				SimpleDeserializers sds = new SimpleDeserializers();
//				sds.addDeserializer(LocalDate.class, new LocalDateDeserializer());
//				context.addDeserializers(sds);

				context.addBeanSerializerModifier(new BeanSerializerModifier() {
					@Override
					public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
						if (beanDesc.getBeanClass() == LocalDate.class) {
							return new LocalDateSerializer();
						}
						return super.modifySerializer(config, beanDesc, serializer);
					}
				});
				context.addBeanDeserializerModifier(new BeanDeserializerModifier() {
					@Override
					public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
						if (beanDesc.getBeanClass() == LocalDate.class) {
							return new LocalDateDeserializer();
						}
						return super.modifyDeserializer(config, beanDesc, deserializer);
					}
				});

//				context.addBeanSerializerModifier(new BeanSerializerModifier() {
//					@Override
//					public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
//						return super.modifySerializer(config, beanDesc, serializer);
//					}
//				});
//				context.addBeanSerializerModifier(new BeanSerializerModifier() {
//					@Override
//					public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
//						return super.modifySerializer(config, beanDesc, serializer);
//					}
//				});

				super.setupModule(context);
			}
		});
		ProtobufSchemaGenerator gen = new ProtobufSchemaGenerator();
		pm.acceptJsonFormatVisitor(User.class, gen);
		pm.acceptJsonFormatVisitor(User2.class, gen);
		ProtobufSchema schemaWrapper = gen.getGeneratedSchema();
		NativeProtobufSchema nativeProtobufSchema = schemaWrapper.getSource();
		String asProtofile = nativeProtobufSchema.toString();
		System.out.println(asProtofile);
		System.out.println((User) pm.reader(schemaWrapper).forType(User.class).readValue(u.toByteArray()));
	}
}
