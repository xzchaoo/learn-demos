package com.xzchaoo.learn.http.httpasyncclient;

import org.apache.http.Consts;
import org.apache.http.HeaderElement;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HttpAsyncClientTest {

	private static final Logger LOG = LoggerFactory.getLogger(HttpAsyncClientTest.class);

	public CloseableHttpAsyncClient makeAHC() throws IOReactorException {
		RequestConfig requestConfig = RequestConfig.custom()
			.setSocketTimeout(3000)
			.setConnectTimeout(3000)
			.setConnectionRequestTimeout(5000)
			.setCookieSpec(CookieSpecs.IGNORE_COOKIES)
			.build();

		// Create I/O reactor configuration
		IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
			.setIoThreadCount(Runtime.getRuntime().availableProcessors())
			//.setIoThreadCount(4)
			.setConnectTimeout(30000)
			.setSoTimeout(30000)
			.setSoKeepAlive(false)
			.build();

		// Create a custom I/O reactort
		ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);

		// Create a connection manager with custom configuration.
		PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
//		connManager.setMaxTotal(256);
//		connManager.setDefaultMaxPerRoute(20);

		// Create message constraints
		MessageConstraints messageConstraints = MessageConstraints.custom()
			.setMaxHeaderCount(40)
			.setMaxLineLength(1024)
			.build();

		// Create connection configuration
		ConnectionConfig connectionConfig = ConnectionConfig.custom()
			.setMalformedInputAction(CodingErrorAction.IGNORE)
			.setUnmappableInputAction(CodingErrorAction.IGNORE)
			.setCharset(Consts.UTF_8)
			.setMessageConstraints(messageConstraints)
			.build();
		// Configure the connection manager to use connection configuration either
		// by default or for a specific host.
		connManager.setDefaultConnectionConfig(connectionConfig);

		// Configure total max or per route limits for persistent connections
		// that can be kept in the pool or leased by the connection manager.
		connManager.setMaxTotal(100);
		connManager.setDefaultMaxPerRoute(10);
		connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);
		CloseableHttpAsyncClient hc = HttpAsyncClientBuilder.create()
			.setConnectionManager(connManager)
			//.setConnectionReuseStrategy(new NoConnectionReuseStrategy())
			.setRoutePlanner(new DefaultRoutePlanner(DefaultSchemePortResolver.INSTANCE) {
				@Override
				public HttpRoute determineRoute(HttpHost host, HttpRequest request, HttpContext context) throws HttpException {
					String hostS = host.getHostName();
					return super.determineRoute(host, request, context);
				}
			})
			.setDefaultRequestConfig(requestConfig)
			.addInterceptorFirst(new HttpRequestInterceptor() {
				@Override
				public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
					HttpClientContext ctx = HttpClientContext.adapt(context);
					System.out.println(context);
				}
			})
			.build();
		return hc;
	}

	@Test
	public void ceshi() throws Exception {
		CloseableHttpAsyncClient ahc = makeAHC();
		ahc.start();

		HttpUriRequest hur = RequestBuilder.get("http://api.bilibili.com").build();
		List<Future> futures = new ArrayList<>(1);
		for (int i = 0; i < 1; ++i) {
			Future<HttpResponse> f = ahc.execute(hur, new FutureCallback<HttpResponse>() {
				public void completed(HttpResponse result) {
					//不要在这里做耗时的动作
					System.out.println(Thread.currentThread());
					try {
						//Thread.sleep(1000);
						long begin = System.currentTimeMillis();
//						System.out.println(result.getClass().getName());
//						System.out.println(result.getEntity().getClass().getName());
						//这里的entity已经被buffer了 org.apache.http.nio.entity.ContentBufferEntity
						String s = EntityUtils.toString(result.getEntity(), StandardCharsets.UTF_8);
						long end = System.currentTimeMillis();
						BasicHeaderElementIterator iter = new BasicHeaderElementIterator(result.headerIterator("Content-Type"));
						while (iter.hasNext()) {
							HeaderElement he = iter.nextElement();
							System.out.println(he.getName() + " " + he.getValue());
						}
						System.out.println(s);
					} catch (Exception e) {
						LOG.error("toString失败", e);
					}
				}

				public void failed(Exception ex) {
					LOG.error("failed", ex);
				}

				public void cancelled() {
					LOG.warn("cancelled");
				}
			});
			futures.add(f);
		}
		futures.forEach(f -> {
			try {
				f.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		});
		//不可以同时消费entity
		//f.get()
		//System.out.println(EntityUtils.toString(f.get().getEntity()));//发生在  String s = EntityUtils.toString(result.getEntity(), StandardCharsets.UTF_8); 结束之前
		ahc.close();
	}
}
