package com.xzchaoo.learn.jmh.e;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.LinkedList;
import java.util.List;

/**
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
@State(Scope.Thread)
public class E26BatchSize {
	List<String> list = new LinkedList<>();

	@Benchmark
	@Warmup(iterations = 5, time = 1)
	@Measurement(iterations = 5, time = 1)
	@BenchmarkMode(Mode.AverageTime)
	public List<String> measureWrong_1() {
		list.add(list.size() / 2, "something");
		System.out.println(list.size());
		return list;
	}

	@Benchmark
	@Warmup(iterations = 5, time = 5)
	@Measurement(iterations = 5, time = 5)
	@BenchmarkMode(Mode.AverageTime)
	public List<String> measureWrong_5() {
		list.add(list.size() / 2, "something");
		System.out.println(list.size());
		return list;
	}


	/*
	 * This is what you do with JMH.
	 */
	@Benchmark
	@Warmup(iterations = 5, batchSize = 5000)
	@Measurement(iterations = 5, batchSize = 5000)
	@BenchmarkMode(Mode.SingleShotTime)
	public List<String> measureRight() {
		list.add(list.size() / 2, "something");
		System.out.println(list.size());
		return list;
	}

	@Setup(Level.Iteration)
	public void setup() {
		list.clear();
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
			.include(E26BatchSize.class.getSimpleName())
			.forks(1)
			.build();

		new Runner(opt).run();
	}

}
