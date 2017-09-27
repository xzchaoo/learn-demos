package com.xzchaoo.learn.fastxmljackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;

import java.io.IOException;

/**
 * Created by Administrator on 2017/4/13.
 */
public class SessionDeserializer extends JsonDeserializer<Session> {
	public Session deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
		ObjectMapper om = (ObjectMapper) p.getCodec();
		JsonNode jsonNode = om.readTree(p);
		JsonNode n = readJsonNode(jsonNode, "a");
		int a = n.asInt(0);
		return new Session(a);
	}

	private JsonNode readJsonNode(JsonNode jsonNode, String field) {
		return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
	}
}
