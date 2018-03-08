package com.xzchaoo.learn.jdk8;

import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {
	@Test
	public void test() {
		List<Integer> list = Stream.of(1, 2, 3, 4)
			.filter(x -> x % 2 == 0)
			.map(x -> x * x)
			.sorted(Comparator.comparingInt((ToIntFunction<Integer>) x -> x).reversed())
			.collect(Collectors.toList());
		System.out.println(list);
	}
}
