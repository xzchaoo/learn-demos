package com.xzchaoo.learn.jmh.example;

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

import java.util.ArrayList;
import java.util.List;

/**
 * 测试rxjava的性能
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 1, batchSize = 1000)
@Measurement(iterations = 3, time = 3, batchSize = 1000)
@Fork(1)
public class IterateTest {
	private int[] array;
	private List<Integer> list;

	@Param({"1024"})
	private int size;

	@Setup
	public void setup() {
		array = new int[size];
		list = new ArrayList<>(size);
		for (int i = 0; i < size; ++i) {
			array[i] = i;
			list.add(i);
		}
	}

	@Benchmark()
	public void array(Blackhole b) {
		long sum = 0;
		for (int i = 0; i < array.length; ++i) {
			sum += array[i];
		}
		b.consume(sum);
	}

	@Benchmark
	public void array_2(Blackhole b) {
		long sum = 0;
		for (int i : array) {
			sum += i;
		}
		b.consume(sum);
	}

	@Benchmark
	public void list(Blackhole b) {
		long sum = 0;
		for (int i = 0; i < list.size(); ++i) {
			sum += list.get(i);
		}
		b.consume(sum);
	}

	@Benchmark
	public void list_2(Blackhole b) {
		long sum = 0;
		int size = list.size();
		for (int i = 0; i < size; ++i) {
			sum += list.get(i);
		}
		b.consume(sum);
	}

	@Benchmark
	public void list_3(Blackhole b) {
		long sum = 0;
		for (int i : list) {
			sum += i;
		}
		b.consume(sum);
	}

	@Benchmark
	public void list_4(Blackhole b) {
		long sum = 0;
		for (Integer i : list) {
			sum += i;
		}
		b.consume(sum);
	}
}
