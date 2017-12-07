package com.xzchaoo.learn.jmh.example;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.xzchaoo.learn.jmh.UserForJson;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;

/**
 * 测试各个json序列化库的性能
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
@State(Scope.Thread)
@Fork(1)
//@Threads(4)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 5)
public class JsonTest {
	private ObjectMapper om;
	private Gson gson;

	@Setup
	public void setup() {
		om = new ObjectMapper();
		om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		gson = new GsonBuilder()
			.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
			.create();
	}

	@State(Scope.Benchmark)
	public static class JsonState {
		public UserForJson user;
		public String json;

		@Setup
		public void setup() {
			user = new UserForJson();
			user.setId(1);
			user.setLong1(1);
			user.setUsername("i am username");
			user.setFloat1(12.456f);
			user.setDouble1(121221.234);
			user.setList1(Arrays.asList("111", "222", "333333"));
			user.setDate1(new Date());
			json = JSON.toJSONString(user);
		}
	}

	@Benchmark
	public void test_fastjson_read(JsonState state, Blackhole b) {
		UserForJson u = JSON.parseObject(state.json, UserForJson.class);
		b.consume(u);
	}

	@Benchmark
	public void test_jackson_read(JsonState state, Blackhole b) throws IOException {
		UserForJson u = om.readValue(state.json, UserForJson.class);
		b.consume(u);
	}

	@Benchmark
	public void test_gson_read(JsonState state, Blackhole b) {
		UserForJson u = gson.fromJson(state.json, UserForJson.class);
		b.consume(u);
	}
}
