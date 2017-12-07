package com.xzchaoo.learn.serialization.jackson.mixin;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.xzchaoo.learn.serialization.jackson.fastxmljackson.entity.SecurityUser;

import java.io.IOException;

/**
 * 自定义反序列化
 */
public class SecurityUserDeserializer extends JsonDeserializer {
	@Override
	public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		//p只具备流式处理
		//它的codec具备tree处理能力
		//读出整个节点
		JsonNode jsonNode = p.getCodec().readTree(p);
		//手动get各个属性
		String name = jsonNode.get("name").asText();
		int age = jsonNode.get("age").asInt();
		int id = jsonNode.get("id").asInt();
		SecurityUser user = new SecurityUser(id, name);
		user.setAge(age);
		return user;
	}
}
