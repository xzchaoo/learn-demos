package com.xzchaoo.learn.consul.api;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.health.model.HealthService;

import org.junit.Test;

import java.util.List;

/**
 * @author xzchaoo
 * @date 2018/1/21
 */
public class ConsulApiApp {
	@Test
	public void test() {
		ConsulClient cc = new ConsulClient("localhost");
		Response<List<HealthService>> response = cc.getHealthServices("consul", false, null,"t2");
		System.out.println(response);
	}
}
