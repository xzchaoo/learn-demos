package com.xzchaoo.learn.fastxmljackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2017/6/15.
 */
public class TreeTest {
	private ObjectMapper om = new ObjectMapper();

	@Test
	public void test_readTree() throws IOException {
		ObjectNode root = (ObjectNode) om.readTree("{\"a\":1,\"b\":\"9\",\"d\":[1,2,\"3\"],\"e\":{\"f\":1}}");
		assertEquals(1, root.get("a").asInt());
		assertEquals("9", root.get("b").asText());
		assertEquals(9, root.get("b").asInt());//支持数字字符串转数字
		assertEquals(1, root.at("/e/f").asInt());//通过at方法可以定位任意深度

		//甚至可以通过root对文档进行修改
		//with方法必须指定一个 对象 如果指定的对象不存在 会自动创建 如果指定的对象不是 json对象 那么异常
		//看返回值就知道要求是一个 ObjectNode
		root.with("e").put("test", "vvv");

		//即使路径不存在
		assertEquals(1, root.at("/no/exist").asInt(1));//通过at方法可以定位任意深度

		System.out.println(root.toString());

	}
}
