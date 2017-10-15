package com.xzchaoo.learn.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 不好用 准备废弃了
 */
public class ConfigTest {
	@Test
	public void test1() {
		//-Dconfig.file
		//系统属性 > application.conf > application.json > application.properties > reference.conf
		//支持一些常见单位 比如 10s
		Config conf = ConfigFactory.load();
		assertEquals(1, conf.getInt("a"));
		assertEquals(22, conf.getInt("b"));

		//如果属性不存在会抛出异常 因此所有配合必须在配置文件里提供值
		try {
			conf.getInt("c");
			assertTrue(false);
		} catch (ConfigException e) {
			assertTrue(true);
		}
	}
}
