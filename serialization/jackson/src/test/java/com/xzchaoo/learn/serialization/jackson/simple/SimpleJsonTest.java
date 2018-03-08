package com.xzchaoo.learn.serialization.jackson.simple;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xzchaoo.learn.serialization.jackson.customserialize.Gender;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * created by xzchaoo at 2017/12/5
 *
 * @author xzchaoo
 */
public class SimpleJsonTest {
	private ObjectMapper om;

	@Before
	public void before() {
		om = new ObjectMapper();
		om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}

	@Test
	public void test() throws IOException {
		String s = "{\"a\":null,\"b\":2,\"gender\":null}";
		O1 o1 = om.readValue(s, O1.class);
		System.out.println(o1);
	}
}

class O1 {
	private int a;
	//似乎不生效
	//@JsonProperty(defaultValue = "MALE", required = true)
	//枚举类型的默认值是比较难处理的 建议通过自定义 反序列化的方式 来手动初始化一些默认值
	private Gender gender;

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "O1{" +
			"a=" + a +
			", gender=" + gender +
			'}';
	}
}