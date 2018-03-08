package com.xzchaoo.learn.db.redis.lettuce;

import org.junit.Test;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.reactive.RedisReactiveCommands;
import io.lettuce.core.masterslave.MasterSlave;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.reactive.RedisPubSubReactiveCommands;

/**
 * @author xzchaoo
 * @date 2017/12/31
 */
public class LettuceTest {
	@Test
	public void test() {
		RedisClient client = RedisClient.create("redis://localhost");
		StatefulRedisPubSubConnection<String, String> ps = client.connectPubSub();
		RedisPubSubReactiveCommands<String, String> psr = ps.reactive();
		psr.subscribe("sdf");
		psr.observeChannels().subscribe();

		StatefulRedisConnection<String, String> connection = client.connect();
		RedisReactiveCommands<String, String> r = connection.reactive();
		System.out.println(r.get("a").block());
	}
}
