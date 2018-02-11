package com.xzchaoo.learn.consul.api;

import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.CoordinateClient;
import com.orbitz.consul.KeyValueClient;

import org.junit.Test;

/**
 * @author xzchaoo
 * @date 2018/1/21
 */
public class ConsulClientTest {
	@Test
	public void test() {
		Consul c = Consul.builder()
			.withAclToken("t2")
			.build(); // connect to Consul on localhost
		KeyValueClient kvc = c.keyValueClient();
		CoordinateClient cc = c.coordinateClient();
		System.out.println(kvc.getValueAsString("a"));
		AgentClient ac = c.agentClient();
		System.out.println(ac.getServices());
	}
}
