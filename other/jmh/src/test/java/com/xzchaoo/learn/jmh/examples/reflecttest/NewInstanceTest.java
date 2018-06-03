package com.xzchaoo.learn.jmh.examples.reflecttest;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * @author xzchaoo
 * @date 2018/6/3
 */
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(1)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class NewInstanceTest {
  private Class<?> clazz;

  @Setup
  public void setup() {
    clazz = Object.class;
  }

  @Benchmark
  public void test_new(Blackhole b) {
    for (int i = 0; i < 100; ++i) {
      b.consume(new Object());
    }
  }

  @Benchmark
  public void test_newInstance(Blackhole b) {
    try {
      for (int i = 0; i < 100; ++i) {
        b.consume(clazz.newInstance());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Benchmark
  public void test_newInstance2(Blackhole b) {
    for (int i = 0; i < 100; ++i) {
      try {
        b.consume(clazz.newInstance());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
