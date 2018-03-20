package com.xzchaoo.learn.jmh.e;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
@State(value = Scope.Benchmark)
public class JMHTest {
	private int n = 10000;

	//表明这是一个b
	@Benchmark
	//可以放在方法或类上
	@BenchmarkMode(Mode.All)
	//修改输出的时间单位
	@OutputTimeUnit(TimeUnit.SECONDS)
	public void array_1() {
		int[] a = new int[n];
		for (int i = 0; i < 100; ++i) {
			for (int j = 0; j < n; ++j) {
				a[j] = i + j;
			}
		}
	}

	@Benchmark
	public void arraylist_1() {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < n; ++i) {
			list.add(0);
		}
		for (int i = 0; i < 100; ++i) {
			for (int j = 0; j < n; ++j) {
				list.set(j, i + j);
			}
		}
	}


	@Benchmark
	@BenchmarkMode(Mode.All)
	@OutputTimeUnit(TimeUnit.SECONDS)
	public void simple2() {
		int s = 0;
		for (int i = 0; i < n; ++i) {
			s += i;
		}
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
			.include(JMHTest.class.getSimpleName())
			.warmupIterations(2)
			.measurementIterations(5)
			.forks(1)
			.build();

		new Runner(opt).run();
	}
}
