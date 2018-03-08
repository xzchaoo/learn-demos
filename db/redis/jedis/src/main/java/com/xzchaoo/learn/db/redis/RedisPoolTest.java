package com.xzchaoo.learn.db.redis;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPoolTest {
	@Test
	public void test1() {
		//https://github.com/xetorthio/jedis/wiki/AdvancedUsage

		JedisPoolConfig cfg = new JedisPoolConfig();
		//可以设置最大连接数之类的参数 和 普通的数据库连接池类似
		JedisPool pool = new JedisPool(cfg, "localhost");
		Jedis jedis = pool.getResource();
		//使用...

		//记得还回去
		jedis.close();

		//或使用
		try ( Jedis jedis2 = pool.getResource() ) {
			jedis2.ping();
		}
		//关闭池
		pool.destroy();
	}
}
