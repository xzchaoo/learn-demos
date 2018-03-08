package com.xzchaoo.learn.jmh.e;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
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
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class E9Blackholes {

	double x1 = Math.PI;
	double x2 = Math.PI * 2;
	@Benchmark
	public double baseline() {
		return Math.log(x1);
	}

    /*
     * While the Math.log(x2) computation is intact, Math.log(x1)
     * is redundant and optimized out.
     */

	@Benchmark
	public double measureWrong() {
		Math.log(x1);
		return Math.log(x2);
	}

    /*
     * This demonstrates Option A:
     *
     * Merge multiple results into one and return it.
     * This is OK when is computation is relatively heavyweight, and merging
     * the results does not offset the results much.
     */

	@Benchmark
	public double measureRight_1() {
		return Math.log(x1) + Math.log(x2);
	}
	@Benchmark
	public void measureRight_2(Blackhole bh) {
		bh.consume(Math.log(x1));
		bh.consume(Math.log(x2));
	}
	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
			.include(E9Blackholes.class.getSimpleName())
			.warmupIterations(5)
			.measurementIterations(5)
			.forks(1)
			.build();

		new Runner(opt).run();
	}
}
