package com.xzchaoo.learn.jmh.example;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * 当并发数达到CPU数量时, 吞吐量为45op/s, 如果此时再调高并发数, 吞吐量依旧维持在45op/s左右, 但此时 平均耗时上去了
 * created by xzchaoo at 2017/12/1
 *
 * @author xzchaoo
 */
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
@Fork(1)
//@Threads(12)
@Threads(48)
@Warmup(iterations = 5)
@Measurement(iterations = 3, time = 4)
public class CpuTest {
	//将n放到local变量可以提高性能
	public int n = 500000000;

	@Benchmark
	public void test1(Blackhole b) {
		long sum = 0;
		for (int i = 0; i < n; ++i) {
			sum += i;
		}
		b.consume(sum);
	}
}
