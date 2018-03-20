package com.xzchaoo.learn.jmh.example;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 * 这个例子比较了 普通的字符串累加 和 使用StringBuilder的性能差异
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
@Warmup(iterations = 3, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
//@BenchmarkMode(Mode.AverageTime)
@BenchmarkMode(Mode.Throughput)
//使用多少个线程来测试 -1表示所有CPU
@Threads(-1)
//fork表示要测试多少次, 每次测试会跑多个迭代
@Fork(1)
@State(Scope.Thread)
public class StringAppendTest {
	@Param({"1", "10", "100", "1000"})
	public int count;

	@Setup
	public void setup() {
		System.out.println("setup");
	}

	@Benchmark
	public void baseline() {
	}

	@Benchmark
	@CompilerControl(CompilerControl.Mode.DONT_INLINE)
	public void baseline_2() {
	}

	@Benchmark
	public void testStringAppend(Blackhole b) {
		String s = "";
		for (int i = 0; i < count; ++i) {
			s += i;
		}
		b.consume(s);
	}

	@Benchmark
	public void testStringBuilderAppend(Blackhole b) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < count; ++i) {
			s.append(i);
		}
		b.consume(s.toString());
	}
}
