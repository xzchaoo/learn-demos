package com.xzchaoo.learn.db.spring.data.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

import redis.clients.jedis.Jedis;

/**
 * redis 仓库 感觉用途不大 这里就不演示了
 */
@SpringBootApplication
public class SpringDataRedisTest implements ApplicationRunner {
	public static void main(String[] args) {
		SpringApplication.run(SpringDataRedisTest.class, args);
	}

	@Autowired
	private StringRedisTemplate srt;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//大部分的redis从操作都可以用srt来完成 有部分操作是没有对应的方法的 可能需要获取到底层的jedis
		//每次用srt执行语句 都不一定是在同一个连接上
		System.out.println(srt.opsForValue().get("a"));

		//获取底层的连接 其实还是封装过了的连接
		srt.execute(new RedisCallback<Void>() {
			@Override
			public Void doInRedis(RedisConnection redisConnection) throws DataAccessException {
				//获得jedis
				Jedis jedis = (Jedis) redisConnection.getNativeConnection();
				return null;
			}
		});

		//如果想让请求在同一个连接上完成 则要 当然了上面的方式也是可以的
		srt.execute(new SessionCallback<Void>() {
			@Override
			public <K, V> Void execute(RedisOperations<K, V> operations) throws DataAccessException {
				//这里需要操作sro而不是srt
				RedisOperations<String, String> sro = (RedisOperations<String, String>) operations;
				String a = sro.opsForValue().get("a");
				return null;
			}
		});

		//你执行了几个写语句 list的大小就是多少
		List<Object> list = srt.executePipelined(new SessionCallback<Void>() {
			@Override
			public <K, V> Void execute(RedisOperations<K, V> operations) throws DataAccessException {
				//在这里进行很多的写操作 注意所有操作的返回值都是null
				RedisOperations<String, String> sro = (RedisOperations<String, String>) operations;
				//如果在这个方法里出现了读操作, 那么读操作其实会放到其他线程上去执行
				//方法的返回结果也是null 反正会被忽略
				return null;
			}
		});
	}
}