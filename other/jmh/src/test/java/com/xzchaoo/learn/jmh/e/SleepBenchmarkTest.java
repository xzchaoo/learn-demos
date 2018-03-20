package com.xzchaoo.learn.jmh.e;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
public class SleepBenchmarkTest {
	@Benchmark
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	public void sleep_1() throws InterruptedException {
		Thread.sleep(1);
	}

	@Benchmark
	@BenchmarkMode(Mode.AverageTime)
	@OutputTimeUnit(TimeUnit.SECONDS)
	public void sleep_10() throws InterruptedException {
		Thread.sleep(10);
	}

	@Benchmark
	@BenchmarkMode(Mode.SampleTime)
	@OutputTimeUnit(TimeUnit.SECONDS)
	public void sleep_100() throws InterruptedException {
		Thread.sleep(100);
	}

	@Benchmark
	@BenchmarkMode(Mode.SingleShotTime)
	@OutputTimeUnit(TimeUnit.SECONDS)
	public void sleep_100_2() throws InterruptedException {
		Thread.sleep(100);
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
			.include(SleepBenchmarkTest.class.getSimpleName())
			.warmupIterations(5)
			.measurementIterations(5)
			.forks(1)
			.build();

		new Runner(opt).run();
	}
}
