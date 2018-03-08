package com.xzchaoo.learn.jctools;

import org.jctools.maps.NonBlockingHashMap;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by xzchaoo at 2017/12/3
 *
 * @author xzchaoo
 */
@BenchmarkMode(Mode.Throughput)
@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class JCToolsTest {

	@State(Scope.Benchmark)
	public static class CHMState {

		public ExecutorService es;

		@Setup
		public void setup() {
			es = Executors.newFixedThreadPool(8);
		}

		@TearDown
		public void tearDown() {
			es.shutdownNow();
		}
	}

	@Benchmark
	public void test_CHM(CHMState state, Blackhole b) throws InterruptedException {
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
		CountDownLatch cdl = new CountDownLatch(8);
		for (int i = 0; i < 4; ++i) {
			state.es.execute(() -> {
				for (int j = 0; j < 1000000; ++j) {
					map.put(Integer.toString(j), Integer.toString(j));
				}
				cdl.countDown();
			});
		}
		for (int i = 0; i < 4; ++i) {
			state.es.execute(() -> {
				Random r = new Random();
				for (int j = 0; j < 1000000; ++j) {
					map.get(Integer.toString(r.nextInt(100000)));
				}
				cdl.countDown();
			});
		}
		cdl.await();
		b.consume(map);
	}

	@Benchmark
	public void test_NBCHM(CHMState state, Blackhole b) throws InterruptedException {
		NonBlockingHashMap<String, String> map = new NonBlockingHashMap<>();
		CountDownLatch cdl = new CountDownLatch(8);
		for (int i = 0; i < 4; ++i) {
			state.es.execute(() -> {
				for (int j = 0; j < 1000000; ++j) {
					map.put(Integer.toString(j), Integer.toString(j));
				}
				cdl.countDown();
			});
		}
		for (int i = 0; i < 4; ++i) {
			state.es.execute(() -> {
				Random r = new Random();
				for (int j = 0; j < 1000000; ++j) {
					map.get(Integer.toString(r.nextInt(100000)));
				}
				cdl.countDown();
			});
		}
		cdl.await();
		b.consume(map);
	}

}