package com.xzchaoo.learn.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

public class HttpClientTest {
	@Test
	public void test_buildRequest() {
		RequestBuilder.post("url")
			.addParameter("a", "b")
			.addHeader("a", "b")
			.setEntity(new StringEntity("abc", ContentType.create("text/plain", "utf-8")))
			.setConfig(RequestConfig.custom().build())
			.build();
	}

	@Test
	public void test_entity() {
		//HttpEntity 封装了 http请求的body
		//常见的表单提交是 UrlEncodedFormEntity
		//一些有用的类 ByteArrayEntity InputStreamEntity FileEntity EntityTemplate
		//如果要构造form-data 就需要引入另外一个jar包 httpmime
		//如果要构造application/json 就使用 StringTemplate 封装一下

		MultipartEntityBuilder b = MultipartEntityBuilder.create();
		b.addTextBody("filename", "abc.png");
		b.addBinaryBody("asdf", new byte[0], ContentType.APPLICATION_OCTET_STREAM, "abc.png");
		HttpEntity entity = b.build();
	}

	@Test
	public void test() {
		RequestConfig defaultRC = RequestConfig.custom()
			.setConnectTimeout(1000)
			.setSocketTimeout(1000)
			.setConnectionRequestTimeout(1000)//这个超时是指从连接池里拿一个连接的超时时间
			.build();
		PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
		manager.setMaxTotal(100);
		manager.setDefaultMaxPerRoute(100);
		CloseableHttpClient chc = HttpClientBuilder.create()
			.setConnectionManager(manager)
			.setDefaultRequestConfig(defaultRC)
			.build();
		CloseableHttpResponse resp = null;
		try {
			HttpUriRequest hur = RequestBuilder.get("http://www.bilibili.com")
				.addHeader("a", "b")
				.addParameter("a", "b")
				.build();
			resp = chc.execute(hur);
			String content = EntityUtils.toString(resp.getEntity());
			StatusLine statusLine = resp.getStatusLine();
			Header[] headers = resp.getAllHeaders();
			System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			HttpClientUtils.closeQuietly(resp);
			HttpClientUtils.closeQuietly(chc);
		}
	}
}
