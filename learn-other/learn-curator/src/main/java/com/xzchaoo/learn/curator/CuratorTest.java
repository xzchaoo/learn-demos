package com.xzchaoo.learn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class CuratorTest {

	private CuratorFramework cf;

	@Before
	public void before() {
		cf = CuratorFrameworkFactory.builder()
			.connectString("127.0.0.1:2181")
			.retryPolicy(new RetryNTimes(3, 1000))
			.build();
		cf.start();
	}

	@After
	public void after() {
		cf.close();
	}

	@Test
	public void test_checkExists() throws Exception {
		Stat stat = cf.checkExists().forPath("/a");
		//不存在则stat为null
		System.out.println(stat);
	}

	@Test
	public void test_checkExists2() throws Exception {
		Stat stat = cf.checkExists().inBackground(new BackgroundCallback() {
			@Override
			public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
				try {
					System.out.println("processResult =" + Thread.currentThread());
					System.out.println("context = " + event.getContext());

					//这里不会有数据
					//System.out.println("data =" + new String(event.getData(), StandardCharsets.UTF_8));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, "i am context").forPath("/a");
		//不存在则stat为null
		System.out.println(stat);
	}

	@Test
	public void test_set() throws Exception {
		//节点不存在会抛异常
		Stat stat = cf.setData().forPath("/a", "你好2".getBytes(StandardCharsets.UTF_8));
		System.out.println(stat);
	}

	@Test
	public void test_get() throws Exception {
		//节点不存在会抛异常
		Stat stat = new Stat();
		byte[] data = cf.getData().storingStatIn(stat).forPath("/b");
		System.out.println(stat);
		System.out.println(data);
	}

	@Test
	public void test_create() throws Exception {
		//节点重复会抛异常
		String result = cf.create().forPath("/a", "你好".getBytes(StandardCharsets.UTF_8));
		System.out.println(result);
	}

	@Test
	public void test_watch() throws Exception {
		byte[] data = cf.getData().usingWatcher(new CuratorWatcher() {
			@Override
			public void process(WatchedEvent event) throws Exception {
				System.out.println(Thread.currentThread());
				System.out.println("process");
				System.out.println(event);
			}
		}).forPath("/a");
		System.out.println("setData");
		System.out.println(cf.setData().forPath("/a", "b".getBytes(StandardCharsets.UTF_8)));
	}

	@Test
	public void test_lock() throws Exception {
		InterProcessMutex lock = new InterProcessMutex(cf, "/lock/aa");
		if (lock.acquire(10, TimeUnit.SECONDS)) {
			try {
				// do some work inside of the critical section here
			} finally {
				lock.release();
			}
		}
	}
}
