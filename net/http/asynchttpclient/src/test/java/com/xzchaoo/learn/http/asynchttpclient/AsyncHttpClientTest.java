package com.xzchaoo.learn.http.asynchttpclient;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.junit.Test;

import lombok.val;

import static org.asynchttpclient.Dsl.asyncHttpClient;


/**
 * 基于netty的一个异步http客户端
 */
public class AsyncHttpClientTest {
  @Test
  public void test1() throws Exception {
    // 支持连接池 支持超时时间 支持请求和响应拦截器
    // 支持CookieStore

    // org.asynchttpclient.Dsl.*;
    val ahcc = new DefaultAsyncHttpClientConfig.Builder()
      .setFollowRedirect(false)
      .setMaxRedirects(0)
      .setConnectTimeout(1000)
      .setReadTimeout(1000)
      .setRequestTimeout(1000)
      // .setMaxConnections(100)
      .setUserAgent("haha")
      .build();
    AsyncHttpClient ahc = asyncHttpClient(ahcc);
    try {
      ListenableFuture<String> f = ahc.prepareGet("https://www.bilibili.com/")
        .addQueryParam("a", "b")
        .execute(new AsyncCompletionHandler<String>() {
          @Override
          public String onCompleted(Response response) throws Exception {
            return response.getResponseBody();
          }
        });
      // 可以利用一些adapter 将 ListenableFuture 转成 Rx系列的Publisher
      System.out.println(f.get());
    } finally {
      ahc.close();
    }
  }
}
