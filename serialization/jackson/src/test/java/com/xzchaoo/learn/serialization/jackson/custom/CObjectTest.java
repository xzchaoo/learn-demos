package com.xzchaoo.learn.serialization.jackson.custom;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonTokenId;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;

/**
 * created by xzchaoo at 2017/10/28
 *
 * @author xzchaoo
 */
public class CObjectTest {
	@Test
	public void test() throws IOException {
		ObjectMapper om = new ObjectMapper();
		om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		//om.disable(DeserializationFeature);
		om.registerModule(new JavaTimeModule());
		SimpleModule sm = new SimpleModule();
		sm.setDeserializerModifier(new BeanDeserializerModifier() {
			@Override
			public JsonDeserializer<?> modifyEnumDeserializer(DeserializationConfig config, JavaType type, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
				Class<? extends Enum> rawClass = (Class<Enum<?>>) type.getRawClass();
				if (rawClass == CGender.class) {
					return new JsonDeserializer<CGender>() {
						@Override
						public CGender deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
							switch (p.getCurrentTokenId()) {
								case JsonTokenId.ID_STRING:
									String value = p.getText();
									for (CGender cg : CGender.values()) {
										if (cg.name().toLowerCase().equals(value.toLowerCase())) {
											return cg;
										}
									}
									break;
								case JsonTokenId.ID_NUMBER_INT:
									int aInt = p.getIntValue();
									return CGender.values()[aInt];
								default:
									throw new IllegalStateException();
							}
							throw new IllegalStateException();
						}
					};
				}
				return deserializer;
			}
		});

		om.registerModule(sm);

		CObject1 co1 = new CObject1();
		co1.setId(1);
		co1.setcGender(CGender.MALE);
		CObject2 co2 = new CObject2();
		co2.setId(2);
		co2.setBirthday(LocalDate.now());
		co1.setCo2(co2);

		String s = om.writeValueAsString(co1);
		System.out.println(s);

		String s1 = "{\"ID\":1,\"cGender\":1,\"co2\":{\"id\":2,\"birthday\":\"2017-10-28\"}}";
		co1 = om.readValue(s1, CObject1.class);
		System.out.println(co1);
	}
}

