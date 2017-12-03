package com.xzchaoo.learn.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * created by xzchaoo at 2017/12/3
 *
 * @author xzchaoo
 */
@State(Scope.Group)
@Measurement(iterations = 3)
@Warmup(iterations = 2)
@Fork(1)
public class ThreadsTest {

	private AtomicBoolean ab = new AtomicBoolean(false);

	@Benchmark
	@Group
	@GroupThreads(6)
	public void test1(Blackhole b) {
		ab.compareAndSet(false, true);
	}

	@Benchmark
	@Group
	@GroupThreads(6)
	public void test2(Blackhole b) {
		ab.compareAndSet(true, false);
	}
}
