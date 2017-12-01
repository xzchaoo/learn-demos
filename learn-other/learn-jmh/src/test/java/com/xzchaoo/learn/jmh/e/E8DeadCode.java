package com.xzchaoo.learn.jmh.e;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

/**
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class E8DeadCode {
	private double x = Math.PI;

	@Benchmark
	public void baseline() {
		// do nothing, this is a baseline
	}

	@Benchmark
	public void measureWrong() {
		// This is wrong: result is not used and the entire computation is optimized away.
		Math.log(x);
	}

	@Benchmark
	public double measureRight() {
		// This is correct: the result is being used.
		return Math.log(x);
	}
}
