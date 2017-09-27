package com.xzchaoo.learn.fastxmljackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.io.IOException;

class User {
	private final int id;
	private final String name;

	/**
	 * 用 JsonCreator 来影响对象的反序列化方式
	 *
	 * @param id
	 * @param name
	 */
	@JsonCreator
	public User(@JsonProperty("id") int id, @JsonProperty("name") String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", name='" + name + '\'' +
			'}';
	}
}

public class JsonCreatorTest {
	@Test
	public void test_JsonCreator() throws IOException {
		User user = new User(1, "xzc");
		ObjectMapper om = new ObjectMapper();
		String json = om.writeValueAsString(user);
		System.out.println(json);
		user = om.readValue(json, User.class);
		System.out.println(user);
	}
}
