package com.xzchaoo.learn.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryForever;
import org.junit.Test;

/**
 * created by xzchaoo at 2017/10/27
 *
 * @author xzchaoo
 */
public class CuratorTest2 {
	@Test
	public void test() throws Exception {
		CuratorFramework cf = CuratorFrameworkFactory.newClient("106.14.175.164:2181", new RetryForever(5000));
		cf.start();
		System.out.println(cf.getData().forPath("/a"));
	}
}
