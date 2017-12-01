package com.xzchaoo.learn.jmh.e;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
//thread 意味着 实例将在同一个线程内共享
//benchmark 意味着实例将在同一个测试的多个线程里共享
//group 意味着只能在同一个线程组里共享
@State(Scope.Thread)
//调整输出的时间单位
@OutputTimeUnit(TimeUnit.MICROSECONDS)
//意思是要跑几次benchmark吗
@Fork(2)
//测试使用的线程数
//@Threads
public class E1Start {
	double x = Math.PI;

	@Benchmark
	@Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
	@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
	public void baseline() {
		//空方法 表明这是一条基线, 用于与其他方法产生对比
	}

	@Benchmark
	@Warmup(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
	@Measurement(iterations = 5, time = 1000, timeUnit = TimeUnit.MILLISECONDS)
	public void wrong() {
		//这个做法是错的, 因为返回值没有被用到 会导致JVM优化掉这个调用
		//通过测试结果也可以看出来 baseline 和 wrong 的性能是一样的
		Math.log(x);
	}
	//预热的迭代次数是5次, 每次持续100毫秒

	//表明这是一个需要测量的方法
	@Benchmark
	//预热的迭代次数是5次, 每次持续100毫秒
	@Warmup(iterations = 5, time = 500, timeUnit = TimeUnit.MILLISECONDS)
	//测量的迭代次数是5次, 每次持续100毫秒
	@Measurement(iterations = 5, time = 2000, timeUnit = TimeUnit.MILLISECONDS)
	//基准测试类型
	@BenchmarkMode(Mode.AverageTime)
	@Threads(4)
	public void test(Blackhole b) {
		//测量log方法的性能 使用b.consume方法的原因是为了防止 JVM 优化掉 Math.log(x) 因为 它的返回值没有被用到
		b.consume(Math.log(x));
	}
}
