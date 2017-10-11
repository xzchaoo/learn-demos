package com.xzchaoo.learn.db.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

import static org.junit.Assert.*;

/**
 * TODO 需要深入了解jedis的连接池机制
 * 利用jedis库连接redis服务器 执行各种redis命令 基本上redis的命令都有对应的 方法, 因此重点是要掌握各种redis命令
 */
public class RedisSingleServerTest {

	//一个jedis就对应了一个连接
	private Jedis jedis;

	@Before
	public void before() {
		jedis = new Jedis("localhost", 6379, false);
		//jedis lazy初始化 必须要执行一条语句才会
		jedis.ping();
	}

	@After
	public void after() {
		jedis.close();
	}

	@Test
	public void test_string() {
		jedis.del("a");
		assertNull(jedis.get("a"));
		jedis.set("a", "a");
		assertEquals("a", jedis.get("a"));
		jedis.append("a", "b");
		assertEquals("ab", jedis.get("a"));

		jedis.del("b");
		assertNull(jedis.get("b"));
		//会自动创建
		jedis.incr("b");
		assertEquals("1", jedis.get("b"));
		jedis.incr("b");
		assertEquals("2", jedis.get("b"));
		jedis.incrBy("b", 2);
		assertEquals("4", jedis.get("b"));
	}

	@Test
	public void test_hash() {
		jedis.del("h");
		Map<String, String> map = jedis.hgetAll("h");
		assertNotNull(map);
		assertTrue(map.isEmpty());
		assertEquals(1L, jedis.hset("h", "f1", "v1").longValue());
		assertEquals(0L, jedis.hset("h", "f1", "v11").longValue());
		Map<String, String> subMaps = new HashMap<>();
		subMaps.put("f2", "v2");
		subMaps.put("f3", "v3");
		assertEquals("OK", jedis.hmset("h", subMaps));
		assertEquals(0L, jedis.hsetnx("h", "f1", "v111").longValue());
		map = jedis.hgetAll("h");
		assertEquals(3, map.size());

		List<String> values = jedis.hmget("h", "f1", "f2");
		assertEquals("v11", values.get(0));
		assertEquals("v2", values.get(1));

		assertEquals(new HashSet<>(Arrays.asList("f1", "f2", "f3")), jedis.hkeys("h"));

		assertTrue(jedis.hexists("h", "f1"));
		assertTrue(jedis.hexists("h", "f2"));
		assertTrue(jedis.hexists("h", "f3"));

		assertEquals(3, jedis.hlen("h").intValue());

		//返回删除的个数
		assertEquals(3L, jedis.hdel("h", "f1", "f2", "f3", "f4").longValue());

		assertFalse(jedis.exists("h"));


	}
}
