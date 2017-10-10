package com.xzchaoo.learn.db.redis;

import org.junit.Test;

import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * TODO 需要深入了解jedis的连接池机制
 * 利用jedis库连接redis服务器 执行各种redis命令 基本上redis的命令都有对应的 方法, 因此重点是要掌握各种redis命令
 */
public class RedisSingleServerTest {
    @Test
    public void test() {
        //jedis lazy初始化 必须要执行一条语句才会
        Jedis jedis = new Jedis("localhost", 6379, false);
        jedis.ping();
        jedis.del("a");
        assertNull(jedis.get("a"));
        jedis.set("a", "a");
        assertEquals("a", jedis.get("a"));
        jedis.close();
    }
}
