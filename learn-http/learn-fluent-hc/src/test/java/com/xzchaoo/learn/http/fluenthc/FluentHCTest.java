package com.xzchaoo.learn.http.fluenthc;

import org.apache.http.client.fluent.Request;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FluentHCTest {
	@Test
	public void test1() throws IOException {
		System.out.println(Request.Get("http://www.bilibili.com")
			.execute()
			.returnContent()
			.asString(StandardCharsets.UTF_8));
	}
}
