package com.xzchaoo.learn.jmh.e;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
public class StateTest {
	//只会在一个benchmark里有效
	@State(Scope.Benchmark)
	public static class BenchmarkState {
		volatile double x = Math.PI;
	}

	@State(Scope.Thread)
	public static class ThreadState {
		volatile double x = Math.PI;
	}

	//每个线程会有它自己的state注入进来, 可以注入多个
	@Benchmark
	public void measureUnshared(ThreadState state) {
		// All benchmark threads will call in this method.
		//
		// However, since ThreadState is the Scope.Thread, each thread
		// will have it's own copy of the state, and this benchmark
		// will measure unshared case.
		state.x++;
	}

	@Benchmark
	public void measureShared(BenchmarkState state) {
		// All benchmark threads will call in this method.
		//
		// Since BenchmarkState is the Scope.Benchmark, all threads
		// will share the state instance, and we will end up measuring
		// shared case.
		state.x++;
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
			.include(StateTest.class.getSimpleName())
			.warmupIterations(5)
			.measurementIterations(5)
			.threads(4)
			.forks(1)
			.build();

		new Runner(opt).run();
	}
}
