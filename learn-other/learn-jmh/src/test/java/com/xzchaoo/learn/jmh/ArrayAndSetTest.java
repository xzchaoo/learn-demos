package com.xzchaoo.learn.jmh;

import com.google.common.collect.Sets;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * 测试结果表明并没有很快
 * 测试在数量比较小的时候的数组和set的性能
 * created by zcxu at 2017/12/6
 *
 * @author zcxu
 */
@Fork(1)
@Measurement(iterations = 5)
@Warmup(iterations = 3)
@BenchmarkMode(Mode.Throughput)
public class ArrayAndSetTest {
	@State(Scope.Benchmark)
	public static class TestState {

		//@Param({"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"})
		@Param({"5"})
		public int size;

		List<String> valid;
		List<String> all;

		@Setup
		public void setup() {
			all = new ArrayList<>();
			Random r = new Random();
			for (int i = 0; i < 200; ++i) {
				all.add(Integer.toString(r.nextInt()));
			}
			Collections.shuffle(all);
			valid = new ArrayList<>(all.subList(0, size));
			Collections.shuffle(all);
		}
	}

	@State(Scope.Benchmark)
	public static class ArrayContainsState {
		String[] array;

		@Setup
		public void setup(TestState state) {
			array = state.valid.toArray(new String[0]);
		}
	}

	@State(Scope.Benchmark)
	public static class SetContainsState {
		Set<String> set;

		@Setup
		public void setup(TestState state) {
			set = new HashSet<>(state.valid);
		}
	}

	@Benchmark
	public void array_add(TestState state, Blackhole b) {
		int size = state.size;
		String[] a = new String[size];
		Random r = new Random(0);
		for (int i = 0; i < size; ++i) {
			a[i] = Long.toString(r.nextLong());
		}
		b.consume(a);
	}

	@Benchmark
	public void set_add(TestState state, Blackhole b) {
		int size = state.size;
		Set<String> set = Sets.newHashSetWithExpectedSize(size);
		Random r = new Random(0);
		for (int i = 0; i < size; ++i) {
			set.add(Long.toString(r.nextLong()));
		}
		b.consume(set);
	}

	@Benchmark
	public void array_contains(TestState testState, ArrayContainsState state, Blackhole b) {
		String[] a = state.array;
		for (String k : testState.all) {
			for (int j = 0; j < a.length; ++j) {
				if (a[j].hashCode() == k.hashCode() && a[j].equals(k)) {
					b.consume(j);
					break;
				}
			}
		}
	}

	@Benchmark
	public void set_contains(TestState testState, SetContainsState state, Blackhole b) {
		Set<String> set = state.set;
		for (String k : testState.all) {
			b.consume(set.contains(k));
		}
	}
}
