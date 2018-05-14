package com.xzchaoo.learn.http.asynchttpclient;

import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.ListenableFuture;
import org.asynchttpclient.Response;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import io.netty.util.concurrent.ImmediateExecutor;
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

    //用CDL异步转同步
    val cdl = new CountDownLatch(1);
    AsyncHttpClient ahc = asyncHttpClient(ahcc);

    ListenableFuture<String> f = ahc.prepareGet("https://www.bilibili.com/")
      .addQueryParam("a", "b")
      .execute(new AsyncCompletionHandler<String>() {
        @Override
        public String onCompleted(Response response) throws Exception {
          return response.getResponseBody();
        }
      });

    f.addListener(() -> {
      try {
        System.out.println("结果是 " + f.get());
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        cdl.countDown();
      }
    }, ImmediateExecutor.INSTANCE);

    cdl.await();
  }
}
