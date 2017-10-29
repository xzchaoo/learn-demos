package com.xzchaoo.learn.serialization.jackson.fastxmljackson.mixin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.xzchaoo.learn.serialization.jackson.fastxmljackson.entity.SecurityUser;

import java.io.IOException;

public class SecurityUserDeserializer extends JsonDeserializer {
	@Override
	public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		JsonNode jsonNode = p.getCodec().readTree(p);
		String name = jsonNode.get("name").asText();
		int age = jsonNode.get("age").asInt();
		int id = jsonNode.get("id").asInt();
		SecurityUser user = new SecurityUser(id, name);
		user.setAge(age);
		return user;
	}
}
