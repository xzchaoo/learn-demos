package com.xzchaoo.learn.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 测试CHM的性能
 *
 * @author zcxu
 * @date 2018/1/10
 */
@Fork(1)
@Measurement(iterations = 5)
@Warmup(iterations = 3)
@BenchmarkMode(Mode.Throughput)
public class ConcurrentHashMapTest {

	@State(Scope.Benchmark)
	public static class SizeState {
		@Param({"10", "100", "1000", "10000", "100000"})
		public int size;
	}

	@State(Scope.Benchmark)
	public static class HashMapState {
		public HashMap<String, String> map;

		@Setup
		public void setup(SizeState sizeState) {
			map = new HashMap<>();
			for (int i = 0; i < sizeState.size; ++i) {
				map.put(Integer.toString(i), Integer.toString(i));
			}
		}
	}

	@State(Scope.Benchmark)
	public static class ConcurrentHashMapState {
		public ConcurrentHashMap<String, String> map;

		@Setup
		public void setup(SizeState sizeState) {
			map = new ConcurrentHashMap<>();
			for (int i = 0; i < sizeState.size; ++i) {
				map.put(Integer.toString(i), Integer.toString(i));
			}
		}
	}

	@Benchmark
	public void hashMap_get(HashMapState state, Blackhole b) {
		Random r = new Random();
		HashMap<String, String> map = state.map;
		int bound = map.size() * 10;
		for (int i = 0; i < 10000; ++i) {
			String key = Integer.toString(r.nextInt(bound));
			String value = map.get(key);
			b.consume(value);
		}
	}

	@Benchmark
	public void concurrentHashMap_get(ConcurrentHashMapState state, Blackhole b) {
		Random r = new Random();
		ConcurrentHashMap<String, String> map = state.map;
		int bound = map.size() * 10;
		for (int i = 0; i < 10000; ++i) {
			String key = Integer.toString(r.nextInt(bound));
			String value = map.get(key);
			b.consume(value);
		}
	}
}
