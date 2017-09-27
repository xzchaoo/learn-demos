package com.xzchaoo.learn.jdk8.forkjoin;

import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Administrator on 2017/3/21.
 */
public class ForkAndJoinApp {
	@Test
	public void test() {
		ForkJoinPool p = ForkJoinPool.commonPool();
		int n = 10;
		Integer result = p.invoke(new RecursiveTask<Integer>() {
			@Override
			protected Integer compute() {
				List<RecursiveTask<Integer>> list = IntStream.range(1, n + 1).mapToObj(id -> new RecursiveTask<Integer>() {
					@Override
					public Integer compute() {
						return id * id;
					}
				}).collect(Collectors.toList());
				list.get(0).fork().join();
				Collection<RecursiveTask<Integer>> recursiveTasks = invokeAll(list);
				int sum = 0;
				for (RecursiveTask<Integer> rt : recursiveTasks) {
					sum += rt.getRawResult();
				}
				return sum;
			}
		});
		System.out.println(Thread.currentThread().getId());
		System.out.println(result);
//		p.shutdown();
//		p.shutdownNow();
	}
}
