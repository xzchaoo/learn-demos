package com.xzchaoo.learn.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 小数组排序
 * created by zcxu at 2017/12/7
 *
 * @author zcxu
 */
@State(Scope.Benchmark)
@Fork(1)
public class SmallListSortTest {
	@Param({"1", "2", "3", "5", "10"})
	int size;
	List<Integer> list;

	@Setup
	public void setup() {
		Random r = new Random(0);
		list = new ArrayList<>(size);
		for (int i = 0; i < size; ++i) {
			list.add(r.nextInt(1000 * size));
		}
	}

	@Benchmark
	public void test(Blackhole b) {
		list.sort(null);
		b.consume(list);
	}
}
