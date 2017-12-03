package com.xzchaoo.learn.jctools;

import org.jctools.maps.ConcurrentAutoTable;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * created by xzchaoo at 2017/12/3
 *
 * @author xzchaoo
 */
@Fork(1)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
@Threads(6)
public class ConcurrentAutoTableTest {
	public static final int N = 1000000;

	@State(Scope.Benchmark)
	public static class AtomicLongState {
		public AtomicLong s = new AtomicLong();
	}

	@State(Scope.Benchmark)
	public static class LongAdderState {
		public LongAdder s = new LongAdder();
	}

	@State(Scope.Benchmark)
	public static class ConcurrentAutoTableState {
		public ConcurrentAutoTable s = new ConcurrentAutoTable();
	}

	@Benchmark
	public void test2(AtomicLongState state, Blackhole b) {
		AtomicLong s = state.s;
		for (int i = 0; i < N; ++i) {
			s.incrementAndGet();
		}
		b.consume(s);
	}

	@Benchmark
	public void test3(LongAdderState state, Blackhole b) {
		LongAdder s = state.s;
		for (int i = 0; i < N; ++i) {
			s.increment();
		}
		b.consume(s.longValue());
	}

	@Benchmark
	public void test4(ConcurrentAutoTableState state, Blackhole b) {
		ConcurrentAutoTable s = state.s;
		for (int i = 0; i < N; ++i) {
			s.increment();
		}
		b.consume(s.longValue());
	}

}
