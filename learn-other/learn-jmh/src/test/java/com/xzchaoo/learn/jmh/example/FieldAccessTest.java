package com.xzchaoo.learn.jmh.example;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 * 用于比较 基于字段访问比较快还是基于方法访问比较快
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
@Warmup(iterations = 2, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.MILLISECONDS)
@BenchmarkMode({Mode.Throughput})
@Fork(1)
@Threads(2)
public class FieldAccessTest {

	//benchmark 表明这个 state 在同一次测量中是被全部线程共享的
	//thread 表明这个 state 在 同一次测量中同一个线程里是共享的 (相当于是每个线程有自己的一个)
	//group 表明这个 state 在 同一次测量中同一个线程组里是共享的, 注意这里是线程组
	@State(Scope.Benchmark)
	public static class User {
		public int x;

		public int getX() {
			return this.x;
		}

		//它可以有它的setup函数 甚和 tearDown
		@Setup
		public void setup(/*这里可以注入其他状态 只要保证最终结果是一个DAG*/) {

		}
	}

	@Benchmark
	public void field(User user, Blackhole b) {
		System.out.println("field " + user + " " + Thread.currentThread().getId());
		b.consume(user.x);
	}

	@Benchmark
	public void getter(User user, Blackhole b) {
		System.out.println("getter " + user + " " + Thread.currentThread().getId());
		b.consume(user.getX());
	}
}
