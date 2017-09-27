package com.xzchaoo.learn.fastxmljackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.xzchaoo.learn.fastxmljackson.entity.User2;

import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/15.
 */
public class FeatureTest extends TestBase {
	private ObjectMapper om = new ObjectMapper();

	@Test
	public void test1() throws Exception {
		om.enable(SerializationFeature.INDENT_OUTPUT);
		om.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		Map<String, Object> obj = new HashMap<>();
		obj.put("a", "1");
		obj.put("b", "c");
		obj.put("ts", new Date());
		System.out.println(om.writeValueAsString(obj));
	}

	@Test
	public void test2() throws IOException {
		JsonFactory jf = new JsonFactory();
		ObjectMapper om = new ObjectMapper(jf);
		om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		//需要注册这个模块才能正确解析
		om.registerModule(new JavaTimeModule());
		String s = om.writeValueAsString(user2);
		System.out.println(s);

		s = "{\"id\":1,\"name\":\"n2\",\"birthday\":\"2017-01-01\",\"money\":4.0,\"active\":true,\"error\":true}";
		User2 user2 = om.readValue(s, User2.class);
		System.out.println(user2);
	}
}
