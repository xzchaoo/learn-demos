package com.xzchaoo.learn.okhttp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * 一个比 httpclient 更加轻量的客户端
 * Created by Administrator on 2017/3/27.
 */
public class App {

	private OkHttpClient client;

	@Before
	public void before() {
		OkHttpClient.Builder b = new OkHttpClient.Builder();
		b.connectTimeout(10, TimeUnit.SECONDS);
		b.readTimeout(10, TimeUnit.SECONDS);
		b.writeTimeout(10, TimeUnit.SECONDS);
		b.addInterceptor(new Interceptor() {
			public Response intercept(Chain chain) throws IOException {
				Request req = chain.request();
				Request.Builder rb = req.newBuilder();
				req = rb
					.build();
				//你有机会编辑req 和 resp
				Response resp = chain.proceed(req);
				return resp;
			}
		});
//		b.addNetworkInterceptor(new Interceptor() {
//			@Override
//			public Response intercept(Chain chain) throws IOException {
//				return null;
//			}
//		});
		client = b.build();
//		WebSocket ws = client.newWebSocket(null, new WebSocketListener() {
//			/**
//			 * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
//			 * messages.
//			 */
//			public void onOpen(WebSocket webSocket, Response response) {
//				System.out.println("ws打开了");
//			}
//
//			/** Invoked when a text (type {@code 0x1}) message has been received. */
//			public void onMessage(WebSocket webSocket, String text) {
//				System.out.println("收到消息了");
//			}
//		});
	}

	public static final MediaType JSON111 = MediaType.parse("application/json; charset=utf-8");

	//@Ignore
	@Test
	public void test_get() throws IOException {
		//url只能自己build

		Request request = new Request.Builder()
			.url("http://api.bilibili.com/x/video?aid=1")
			.build();

		//RequestBody.create()
		new FormBody.Builder().add("a", "a").build();

		//new MultipartBody.Builder().build()

		client.newCall(request).enqueue(new Callback() {
			public void onFailure(Call call, IOException e) {
			}

			public void onResponse(Call call, Response response) throws IOException {

			}
		});

		Response response = client.newCall(request).execute();
		Headers hs = response.headers();
		for (String name : hs.names()) {
			System.out.println(name + "=" + hs.get(name));
		}
		System.out.println(response.header("Connection"));
		String s = response.body().string();
		JSONObject r = JSON.parseObject(s);
		System.out.println(r);
		//assertEquals("公告", r.getJSONObject("data").getString("typename"));
	}

	@Test
	public void test_header() {
		Request request = new Request.Builder()
			.url("https://api.bilibili.com/x/video?aid=1")
			.header("a", "1")
			.header("b", "2")
			.build();
		assertEquals("1", request.header("a"));
		assertEquals("2", request.header("b"));
		//RequestBody.create(MediaType.parse(""))
	}

	@Test
	public void test_post_form() throws IOException {
		RequestBody formBody = new FormBody.Builder()
			.add("search", "Jurassic Park")
			.build();
		Request request = new Request.Builder()
			.url("https://en.wikipedia.org/w/index.php")
			.post(formBody)
			.build();
		String s = client.newCall(request).execute().body().string();
		System.out.println(s);
	}

	@Test
	public void 临时客户端() {
		OkHttpClient.Builder b = client.newBuilder();
		//做修改
		//b.writeTimeout()
		OkHttpClient c = b.build();
		//new Request.Builder().get().url("asdf")
	}

	@Test
	public void test_HttpUrl() {
		HttpUrl.Builder b = new HttpUrl.Builder();
		b.scheme("http");
		b.host("api.bilibili.com");
		b.encodedPath("/x/video");
		b.addQueryParameter("aid", Integer.toString(1));
		b.addQueryParameter("name", "测试");
		HttpUrl hu = b.build();
		System.out.println(hu);
	}

	@Test
	public void test_HttpUrl_2() {
		String url = "http://api.bilibili.com/x/video?aid=1&name=%E6%B5%8B%E8%AF%95";
		HttpUrl hu = HttpUrl.parse(url);
		assertEquals("http", hu.scheme());
		assertEquals("api.bilibili.com", hu.host());
		assertEquals(80, hu.port());
		assertEquals("/x/video", hu.encodedPath());
		assertEquals("1", hu.queryParameter("aid"));
		assertEquals("测试", hu.queryParameter("name"));
	}
}