package com.xzchaoo.learn.http.asynchttpclient;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.AsyncHttpClientConfig;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.junit.Test;

import java.util.concurrent.Future;

public class AsyncHttpClientTest {
	@Test
	public void test1() throws Exception {
		AsyncHttpClientConfig ahcc = new DefaultAsyncHttpClientConfig.Builder()
			.setFollowRedirect(false)
			.setMaxRedirects(0)
			.setConnectTimeout(1000)
			.setReadTimeout(1000)
			.setRequestTimeout(1000)
			.setUserAgent("")
			.build();
		DefaultAsyncHttpClient ahc = new DefaultAsyncHttpClient(ahcc);
		ListenableFuture<Integer> f = ahc.prepareGet("http://www.bilibili.com/")
			.execute(new AsyncCompletionHandler<Integer>() {
				@Override
				public Integer onCompleted(Response response) throws Exception {
					String body = response.getResponseBody();
					Thread.sleep(5000);
					System.out.println(body);
					return 1;
				}
			});
		Integer integer = f.get();
		System.out.println(integer);
	}
}
