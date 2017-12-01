package com.xzchaoo.learn.jmh.e;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

/**
 * http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class E11Loops {
	    /*
     * It would be tempting for users to do loops within the benchmarked method.
     * (This is the bad thing Caliper taught everyone). These tests explain why
     * this is a bad idea.
     *
     * Looping is done in the hope of minimizing the overhead of calling the
     * test method, by doing the operations inside the loop instead of inside
     * the method call. Don't buy this argument; you will see there is more
     * magic happening when we allow optimizers to merge the loop iterations.
     */

    /*
     * Suppose we want to measure how much it takes to sum two integers:
     */

	int x = 1;
	int y = 2;

    /*
     * This is what you do with JMH.
     */

	@Benchmark
	public int measureRight() {
		return (x + y);
	}

	/*
	 * The following tests emulate the naive looping.
	 * This is the Caliper-style benchmark.
	 */
	private int reps(int reps) {
		int s = 0;
		for (int i = 0; i < reps; i++) {
			s += (x + y);
		}
		return s;
	}

    /*
     * We would like to measure this with different repetitions count.
     * Special annotation is used to get the individual operation cost.
     */

	@Benchmark
	@OperationsPerInvocation(1)
	public int measureWrong_1() {
		return reps(1);
	}

	@Benchmark
	@OperationsPerInvocation(10)
	public int measureWrong_10() {
		return reps(10);
	}

	@Benchmark
	@OperationsPerInvocation(100)
	public int measureWrong_100() {
		return reps(100);
	}

	@Benchmark
	@OperationsPerInvocation(1000)
	public int measureWrong_1000() {
		return reps(1000);
	}

	@Benchmark
	@OperationsPerInvocation(10000)
	public int measureWrong_10000() {
		return reps(10000);
	}

	@Benchmark
	@OperationsPerInvocation(100000)
	public int measureWrong_100000() {
		return reps(100000);
	}
}
