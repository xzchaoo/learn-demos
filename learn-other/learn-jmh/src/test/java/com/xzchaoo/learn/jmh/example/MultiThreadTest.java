package com.xzchaoo.learn.jmh.example;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zcxu
 * @date 2018/2/28 0028
 */
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(1)
@State(Scope.Group)
public class MultiThreadTest {
  AtomicInteger counter;

  @Setup
  public void setup() {
    counter = new AtomicInteger(0);
  }

  @TearDown
  public void tearDown() {
    assert counter.get() > 0;
  }

  //同一个g里的
  @Group("g")
  @GroupThreads(3)
  @Benchmark
  public void inc() {
    counter.incrementAndGet();
  }

  @Group("g")
  @GroupThreads(1)
  @Benchmark
  public void dec() {
    counter.decrementAndGet();
  }
}
