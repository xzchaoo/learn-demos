package com.xzchaoo.learn.fastxmljackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.StringWriter;

/**
 * Created by Administrator on 2017/6/15.
 */
public class StreamTest {
	private ObjectMapper om = new ObjectMapper();

	@Test
	public void test_write() throws Exception {
		JsonFactory f = om.getFactory();
		StringWriter sw = new StringWriter();
		JsonGenerator jg = f.createGenerator(sw);
		jg.writeStartObject();
		jg.writeNumberField("a", 1);
		jg.writeObjectFieldStart("b");
		jg.writeArrayFieldStart("c");
		jg.writeNumber(1);
		jg.writeNumber(2);
		jg.writeBoolean(false);
		jg.writeEndArray();
		jg.writeEndObject();
		jg.writeEndObject();
		jg.flush();
		jg.close();
		System.out.println(sw.toString());
	}

	@Test
	public void test_read() throws Exception {
		String str = "{\"a\":1,\"b\":{\"c\":[1,2,false]}}";
		JsonFactory f = om.getFactory();
		JsonParser p = f.createParser(str);
		while (true) {
			JsonToken t = p.nextToken();
			System.out.println(t);
			if (t == null) {
				break;
			}
		}
	}
}
