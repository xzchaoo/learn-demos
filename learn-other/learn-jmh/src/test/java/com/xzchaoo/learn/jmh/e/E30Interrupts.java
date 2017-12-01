package com.xzchaoo.learn.jmh.e;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Group)
public class E30Interrupts {
	private BlockingQueue<Integer> q;

	@Setup
	public void setup() {
		q = new ArrayBlockingQueue<>(1);
	}

	@Group("Q")
	@Benchmark
	public Integer take() throws InterruptedException {
		return q.take();
	}

	@Group("Q")
	@Benchmark
	public void put() throws InterruptedException {
		q.put(42);
	}

	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
			.include(E30Interrupts.class.getSimpleName())
			.warmupIterations(5)
			.measurementIterations(5)
			.threads(2)
			.forks(5)
			.timeout(TimeValue.seconds(5))
			.build();

		new Runner(opt).run();
	}
}
