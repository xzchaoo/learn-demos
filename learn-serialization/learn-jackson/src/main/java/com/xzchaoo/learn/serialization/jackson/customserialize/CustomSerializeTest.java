package com.xzchaoo.learn.serialization.jackson.customserialize;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.module.SimpleModule;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * created by xzchaoo at 2017/10/29
 *
 * @author xzchaoo
 */
public class CustomSerializeTest {
	@Test
	public void test1() throws IOException {
		ObjectMapper om = new ObjectMapper();
		//忽略未知
		om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		SimpleModule sm = new SimpleModule();
		sm.setDeserializerModifier(new BeanDeserializerModifier() {
			@Override
			public List<BeanPropertyDefinition> updateProperties(DeserializationConfig config, BeanDescription beanDesc, List<BeanPropertyDefinition> propDefs) {
				System.out.println(config);
				System.out.println(beanDesc);
				System.out.println(propDefs);
				return super.updateProperties(config, beanDesc, propDefs);
			}

			@Override
			public JsonDeserializer<?> modifyEnumDeserializer(DeserializationConfig config, JavaType type, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
				System.out.println("modifyEnumDeserializer");
				return super.modifyEnumDeserializer(config, type, beanDesc, deserializer);
			}
		});
		om.registerModule(sm);


		Object1 o1 = new Object1();
		o1.setId(1);
		o1.setGender(Gender.MALE);
		o1.setGender2(Gender.FEMALE);
		o1.setBirthday(LocalDate.now());
		String s = om.writeValueAsString(o1);
		System.out.println(s);
		o1 = om.readValue(s, Object1.class);
		System.out.println(o1);
		System.out.println(om.writeValueAsString(o1));


		s = "{\"Gender\":0,\"gender2\":\"FEMALE\",\"birthday\":\"B20171029\",\"id2\":1}";
		System.out.println(om.readValue(s, Object1.class));
	}
}
