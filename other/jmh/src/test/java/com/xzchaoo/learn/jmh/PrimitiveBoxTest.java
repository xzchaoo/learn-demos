package com.xzchaoo.learn.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * @author zcxu
 * @date 2018/1/10
 */
@Fork(1)
@Measurement(iterations = 5)
@Warmup(iterations = 3)
@BenchmarkMode(Mode.Throughput)
public class PrimitiveBoxTest {

	@State(Scope.Benchmark)
	public static class BoxState {
		private int count = 1000;
	}

	@OperationsPerInvocation
	@Benchmark
	public void array_add1(BoxState state, Blackhole b) {
		int count = state.count;
		int sum = 0;
		for (int i = 0; i < count; ++i) {
			sum += i;
		}
		b.consume(sum);
	}

	@OperationsPerInvocation
	@Benchmark
	public void array_add2(BoxState state, Blackhole b) {
		int count = state.count;
		Integer sum = 0;
		for (int i = 0; i < count; ++i) {
			sum += i;
		}
		b.consume(sum);
	}

}
