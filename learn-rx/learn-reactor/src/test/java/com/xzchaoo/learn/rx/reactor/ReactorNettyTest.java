package com.xzchaoo.learn.rx.reactor;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.nio.charset.Charset;

import reactor.core.publisher.Mono;
import reactor.ipc.netty.ByteBufFlux;
import reactor.ipc.netty.http.client.HttpClient;
import reactor.ipc.netty.http.client.HttpClientResponse;

/**
 * @author xzchaoo
 * @date 2017/12/31
 */
public class ReactorNettyTest {
	@Test
	public void test() throws InterruptedException {
		HttpClient hc = HttpClient.create();
		Mono<HttpClientResponse> m = hc.get("http://www.qq.com");
		String s = m.flatMap(resp -> {
			String contentType = resp.responseHeaders().get("Content-Type");
			System.out.println(contentType);
			String charset = StringUtils.substringAfter(contentType, "=");
			return ByteBufFlux.fromInbound(resp.receiveContent()).aggregate().asString(Charset.forName(charset));
		}).block();
		System.out.println(s);
		Thread.sleep(1000);
	}
}
