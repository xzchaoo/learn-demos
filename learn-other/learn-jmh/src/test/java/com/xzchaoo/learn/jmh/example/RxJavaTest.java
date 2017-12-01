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

import io.reactivex.Flowable;

/**
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 3)
public class RxJavaTest {
	public List<Integer> list;

	@Param({"1024000"})
	public int size;

	@Setup
	public void setup() {
		list = new ArrayList<>(size);
		for (int i = 0; i < size; ++i) {
			list.add(i);
		}
	}


	@Benchmark
	public void list(Blackhole b) {
		long sum = 0;
		for (int i = 0; i < list.size(); ++i) {
			int x = list.get(i);
			if (x % 7 == 0) {
				sum += x * 2;
			}
		}
		b.consume(sum);
	}

	@Benchmark
	public void fromIterator(Blackhole b) {
		long[] sum = new long[]{0};
		Flowable.fromIterable(list).filter(x -> x % 7 == 0).map(x -> x * x).forEach(x -> sum[0] += x);
		b.consume(sum);
	}

	@Benchmark
	public void fromIterator_2(Blackhole b) {
		long[] sum = new long[]{0};
		Flowable.fromIterable(list).forEach(x -> {
			if (x % 7 == 0) {
				sum[0] += x * x;
			}
		});
		b.consume(sum);
	}

	@Benchmark
	public void stream(Blackhole b) {
		long[] sum = new long[]{0};
		list.stream().filter(x -> x % 7 == 0).map(x -> x * x).forEach(x -> sum[0] += x);
		b.consume(sum);
	}
}
