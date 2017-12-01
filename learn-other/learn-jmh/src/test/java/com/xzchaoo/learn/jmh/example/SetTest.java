package com.xzchaoo.learn.jmh.example;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

/**
 * created by xzchaoo at 2017/12/1
 *
 * @author xzchaoo
 */
@BenchmarkMode(Mode.SampleTime)
@Fork(1)
@Warmup(iterations = 2, time = 2)
@Measurement(iterations = 3, time = 3)
@State(Scope.Benchmark)
public class SetTest {

	public int n = 1000000;

	@Benchmark
	public void treeset(Blackhole b) {
		Random r = new Random(1234);
		Set<String> set = new TreeSet<>();
		for (int i = 0; i < n; ++i) {
			set.add(Integer.toString(r.nextInt()));
		}
		b.consume(set);
	}

	@Benchmark
	public void hashset(Blackhole b) {
		Random r = new Random(1234);
		Set<String> set = new HashSet<>();
		for (int i = 0; i < n; ++i) {
			set.add(Integer.toString(r.nextInt()));
		}
		b.consume(set);
	}


	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
			.include(SetTest.class.getSimpleName())
			.warmupIterations(5)
			.measurementIterations(5)
			.threads(4)
			.forks(1)
			.build();
		new Runner(opt).run();
	}
}
