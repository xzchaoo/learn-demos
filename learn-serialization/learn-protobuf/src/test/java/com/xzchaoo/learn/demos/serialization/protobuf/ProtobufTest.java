package com.xzchaoo.learn.demos.serialization.protobuf;

import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;
import com.fasterxml.jackson.dataformat.protobuf.schema.NativeProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchema;
import com.fasterxml.jackson.dataformat.protobuf.schemagen.ProtobufSchemaGenerator;
import com.xzhcaoo.learn.LocalDateDeserializer;
import com.xzhcaoo.learn.LocalDateSerializer;
import com.xzhcaoo.learn.User;
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
				SimpleSerializers sss = new SimpleSerializers();
				sss.addSerializer(LocalDate.class, new LocalDateSerializer());
				context.addSerializers(sss);
				SimpleDeserializers sds = new SimpleDeserializers();
				sds.addDeserializer(LocalDate.class, new LocalDateDeserializer());
				context.addDeserializers(sds);
//				context.addTypeModifier(new TypeModifier() {
//					@Override
//					public JavaType modifyType(JavaType type, Type jdkType, TypeBindings context, TypeFactory typeFactory) {
//						if (type.getRawClass() == LocalDate.class) {
//							return typeFactory.constructType(String.class);
//						}
//						System.out.println("heresdfsdfdsf");
//						return type;
//					}
//				});
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
		ProtobufSchema schemaWrapper = gen.getGeneratedSchema();
		NativeProtobufSchema nativeProtobufSchema = schemaWrapper.getSource();
		String asProtofile = nativeProtobufSchema.toString();
		System.out.println(asProtofile);
		System.out.println((User) pm.reader(schemaWrapper).forType(User.class).readValue(u.toByteArray()));
	}
}
