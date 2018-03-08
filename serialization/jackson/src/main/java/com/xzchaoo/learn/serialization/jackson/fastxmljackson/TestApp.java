package com.xzchaoo.learn.serialization.jackson.fastxmljackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.xzchaoo.learn.serialization.jackson.fastxmljackson.entity.Error;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by Administrator on 2017/4/13.
 */
public class TestApp {
	@Test
	public void t1() throws IOException {
		ObjectMapper om = new ObjectMapper();
		SimpleModule sm = new SimpleModule();
		sm.addDeserializer(Session.class, new SessionDeserializer());
		om.registerModule(sm);
		Session s = new Session(1);
		s.setA(2);
		System.out.println(s);
		String v = om.writeValueAsString(s);
		System.out.println(v);
		s = om.readValue(v, Session.class);
		System.out.println(s);
	}

	@Test
	public void test_json_node() throws IOException {
		ObjectMapper om = new ObjectMapper();
		String str = "{\"error\":{\"code\":1,\"msg\":\"test\"}}";
		//JsonNode jn = om.readTree(str);
		Error er = om.reader().at("/error").forType(Error.class).readValue(str);
		System.out.println(er);
		//System.out.println(jn.at("/error").asInt());
	}
}
