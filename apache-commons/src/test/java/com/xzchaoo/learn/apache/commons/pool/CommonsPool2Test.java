package com.xzchaoo.learn.apache.commons.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;

/**
 * 对象池
 */
public class CommonsPool2Test {
	@Test
	public void test() throws Exception {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxTotal(10);
		config.setMaxIdle(10);
		config.setMaxWaitMillis(30000);
		config.setMinIdle(4);
		config.setTestWhileIdle(true);
		config.setNumTestsPerEvictionRun(2);
		config.setTimeBetweenEvictionRunsMillis(10000);
		config.setMinEvictableIdleTimeMillis(10000);

		BasePooledObjectFactory factory = new BasePooledObjectFactory<String>() {
			@Override
			public String create() throws Exception {
				System.out.println("create");
				return new String("default");
			}

			@Override
			public PooledObject wrap(String obj) {
				return new DefaultPooledObject<>(obj);
			}
		};
		GenericObjectPool<String> p = new GenericObjectPool<String>(
			factory,
			config
		);
		for (int i = 0; i < 11; ++i) {
			int ii = i;
			String s = p.borrowObject();
			new Thread(() -> {
				try {
					Thread.sleep((1 + ii) * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				p.returnObject(s);
			}).start();
		}
		System.out.println("end");
	}
}
