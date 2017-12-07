package com.xzchaoo.learn.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 测试当list数量比较小的时候的迭代效率 效率高一点点...
 * created by zcxu at 2017/12/7
 *
 * @author zcxu
 */
@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@State(Scope.Benchmark)
public class SmallListIteratorTest {
	List<Integer> list;

	@Setup
	public void setup() {
		list = new ArrayList<>(2);
		list.add(1);
		list.add(2);
	}

	@Benchmark
	public void get0(Blackhole b) {
		b.consume(list.get(0));
		b.consume(list.get(1));
	}

	@Benchmark
	public void iter1(Blackhole b) {
		Iterator<Integer> iter = list.iterator();
		b.consume(iter.next());
		b.consume(iter.next());
	}

	@Benchmark
	public void iter2(Blackhole b) {
		for (Integer i : list) {
			b.consume(i);
		}
	}
}
