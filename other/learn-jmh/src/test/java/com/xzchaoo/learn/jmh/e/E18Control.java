package com.xzchaoo.learn.jmh.e;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Control;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
@State(Scope.Group)
public class E18Control {
	public AtomicBoolean flag = new AtomicBoolean();

	//通过control 方法可以感知 JMH 的停止意图, 从而停下来

	@Benchmark
	@Group("pingpong")
	public void ping(Control ctl) {
		while (!ctl.stopMeasurement && flag.compareAndSet(false, true)) {
		}
	}

	@Benchmark
	@Group("pingpong")
	public void pong(Control ctl) {
		while (!ctl.stopMeasurement && flag.compareAndSet(true, false)) {
		}
	}


	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
			.include(E18Control.class.getSimpleName())
			.warmupIterations(1)
			.measurementIterations(5)
			.threads(2)
			.forks(1)
			.build();

		new Runner(opt).run();
	}
}
