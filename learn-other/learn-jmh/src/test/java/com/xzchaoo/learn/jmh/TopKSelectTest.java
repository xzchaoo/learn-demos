package com.xzchaoo.learn.jmh;

import com.google.common.collect.Comparators;
import com.google.common.collect.Ordering;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * TODO 这里有保证选出的结果一定是有序的吗
 * ObjectHeapPriorityQueue
 * 从一个数组里选出最小的K个元素
 * created by zcxu at 2017/12/7
 *
 * @author zcxu
 */
@Fork(1)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@State(Scope.Benchmark)
public class TopKSelectTest {
	private int size = 5000000;
	private int k = 10000;

	List<Integer> list;

	@Setup
	public void setup() {
		list = new ArrayList<>(size);
		Random r = new Random(0);
		for (int i = 0; i < size; ++i) {
			list.add(r.nextInt(1000000));
		}
	}

	@Benchmark
	public void qsort(Blackhole b) {
		//排序取topK 效率肯定是最差的 O(nlogn)
		List<Integer> list = new ArrayList<>(this.list);
		list.sort(null);
		list = list.subList(0, k);
		b.consume(list);
	}

	@Benchmark
	public void google(Blackhole b) {
		//内部实现不明
		List<Integer> list = new ArrayList<>(this.list);
		List<Integer> result = Ordering.natural().leastOf(list, k);
		b.consume(result);
	}

	@Benchmark
	public void priorityQueue(Blackhole b) {
		//看介绍应该是基于堆的
		//PriorityQueue没有重新实现addAll方法 它的效率是比较低的 可以在构造函数将所有值直接传进去
		//这个方法效率是最高的
		PriorityQueue<Integer> pq = new PriorityQueue<>(list);
		List<Integer> result = new ArrayList<>(k);
		for (int i = 0; i < k; ++i) {
			result.add(pq.poll());
		}
		b.consume(result);
	}

	@Benchmark
	public void test_stream(Blackhole b) {
		b.consume(list.stream().collect(Comparators.least(1, Integer::compare)));
	}

	@Benchmark
	public void priorityQueue2(Blackhole b) {
		//看介绍应该是基于堆的
		//PriorityQueue没有重新实现addAll方法 它的效率是比较低的 可以在构造函数将所有值直接传进去
		//这个方法效率是最高的
		PriorityQueue<Integer> pq = new PriorityQueue<>(k);
		pq.addAll(list);
		List<Integer> result = new ArrayList<>(k);
		for (int i = 0; i < 1000; ++i) {
			result.add(pq.poll());
		}
		b.consume(result);
	}

	@Benchmark
	public void test_TopKHeap(Blackhole b) {
		TopKHeapArray<Integer> heap = new TopKHeapArray<Integer>(k, Integer::compare);
		for (Integer x : list) {
			heap.add(x);
		}
		b.consume(heap.getBuffer());
	}
}
