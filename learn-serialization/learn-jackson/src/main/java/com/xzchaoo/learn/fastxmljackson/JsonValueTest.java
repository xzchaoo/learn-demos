package com.xzchaoo.learn.fastxmljackson;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class JsonValueTest {
	@JsonClassDescription("我是描述")
	private static class User {
		private int id;
		private String name;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		/**
		 * 用于定制本对象的序列化
		 *
		 * @return
		 */
		@JsonValue
		//@JsonRawValue
		public Object getAsJsonObject() {
			return Collections.singletonMap("code", 0);
		}

		@Override
		public String toString() {
			return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
		}
	}

	@Test
	public void test_JsonValue() throws JsonProcessingException {
		User user = new User();
		user.setId(1);
		user.setName("xzc");
		assertEquals("{\"code\":0}", new ObjectMapper().writeValueAsString(user));
	}
}
