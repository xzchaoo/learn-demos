package com.xzchaoo.learn.jmh.examples.plusplustest;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * 测试前置++ 和 后置++的性能
 *
 * @author zcxu
 * @date 2018/3/5 0005
 */
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@Fork(1)
@State(Scope.Benchmark)
public class PlusPlusTest {
  private int n = 1000_0000;

  @Benchmark
  public void test1(Blackhole b) {
    int n = this.n;
    long sum = 0;
    for (int i = 0; i < n; ++i) {
      sum += i;
    }
    b.consume(sum);
  }

  @Benchmark
  public void test2(Blackhole b) {
    int n = this.n;
    long sum = 0;
    for (int i = 0; i < n; i++) {
      sum += i;
    }
    b.consume(sum);
  }

  @Benchmark
  public void test3(Blackhole b) {
    int n = this.n;
    long sum = 0;
    for (Integer i = 0; i < n; ++i) {
      sum += i;
    }
    b.consume(sum);
  }

  @Benchmark
  public void test4(Blackhole b) {
    int n = this.n;
    long sum = 0;
    for (Integer i = 0; i < n; i++) {
      sum += i;
    }
    b.consume(sum);
  }
}
