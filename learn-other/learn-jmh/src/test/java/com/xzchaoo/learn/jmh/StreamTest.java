package com.xzchaoo.learn.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.List;

/**
 * @author zcxu
 * @date 2018/1/17
 */

@Fork(1)
@Measurement()
@Warmup
public class StreamTest {
	@State(Scope.Benchmark)
	public static class ArrayState {
		int[] array;
	}
	@State(Scope.Benchmark)
	public static class ListState {
		List<Integer> list;
	}

	@Benchmark
	public void test_array_1() {
		//int[] ...
	}

	@Benchmark
	public void test_array_2() {
		//List<T> ...
	}

	@Benchmark
	public void test_array_3() {
		//
	}

	@Benchmark
	public void test_2() {

	}
}
