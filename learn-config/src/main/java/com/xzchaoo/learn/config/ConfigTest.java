package com.xzchaoo.learn.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigTest {
	@Test
	public void test1() {
		//-Dconfig.file
		//系统属性 > application.conf > application.json > application.properties > reference.conf
		//支持一些常见单位 比如 10s
		Config conf = ConfigFactory.load();
		assertEquals(1, conf.getInt("a"));
		assertEquals(22, conf.getInt("b"));

		//如果属性不存在会抛出异常
		//conf.getInt("c");
	}
}
