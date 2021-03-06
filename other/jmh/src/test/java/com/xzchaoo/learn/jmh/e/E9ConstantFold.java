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
public class E9ConstantFold {
	    /*
     * The flip side of dead-code elimination is constant-folding.
     *
     * If JVM realizes the result of the computation is the same no matter what,
     * it can cleverly optimize it. In our case, that means we can move the
     * computation outside of the internal JMH loop.
     *
     * This can be prevented by always reading the inputs from non-final
     * instance fields of @State objects, computing the result based on those
     * values, and follow the rules to prevent DCE.
     */

	// IDEs will say "Oh, you can convert this field to local variable". Don't. Trust. Them.
	// (While this is normally fine advice, it does not work in the context of measuring correctly.)
	private double x = Math.PI;

	// IDEs will probably also say "Look, it could be final". Don't. Trust. Them. Either.
	// (While this is normally fine advice, it does not work in the context of measuring correctly.)
	private final double wrongX = Math.PI;

	@Benchmark
	public double baseline() {
		// simply return the value, this is a baseline
		return Math.PI;
	}

	@Benchmark
	public double measureWrong_1() {
		// This is wrong: the source is predictable, and computation is foldable.
		return Math.log(Math.PI);
	}

	@Benchmark
	public double measureWrong_2() {
		// This is wrong: the source is predictable, and computation is foldable.
		return Math.log(wrongX);
	}

	@Benchmark
	public double measureRight() {
		// This is correct: the source is not predictable.
		return Math.log(x);
	}
}
