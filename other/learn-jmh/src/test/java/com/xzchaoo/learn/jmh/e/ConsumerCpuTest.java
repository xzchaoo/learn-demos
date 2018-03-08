package com.xzchaoo.learn.jmh.e;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

/**
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
public class ConsumerCpuTest {
	@Benchmark
	public void consume_0000() {
		Blackhole.consumeCPU(0);
	}

	@Benchmark
	public void consume_0001() {
		Blackhole.consumeCPU(1);
	}

	@Benchmark
	public void consume_0002() {
		Blackhole.consumeCPU(2);
	}

	@Benchmark
	public void consume_0004() {
		Blackhole.consumeCPU(4);
	}

	@Benchmark
	public void consume_0008() {
		Blackhole.consumeCPU(8);
	}

	@Benchmark
	public void consume_0016() {
		Blackhole.consumeCPU(16);
	}

	@Benchmark
	public void consume_0032() {
		Blackhole.consumeCPU(32);
	}

	@Benchmark
	public void consume_0064() {
		Blackhole.consumeCPU(64);
	}

	@Benchmark
	public void consume_0128() {
		Blackhole.consumeCPU(128);
	}

	@Benchmark
	public void consume_0256() {
		Blackhole.consumeCPU(256);
	}

	@Benchmark
	public void consume_0512() {
		Blackhole.consumeCPU(512);
	}

	@Benchmark
	public void consume_1024() {
		Blackhole.consumeCPU(1024);
	}
}
