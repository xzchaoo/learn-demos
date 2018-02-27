package com.xzchaoo.learn.jmh.e;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.infra.Control;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zcxu
 * @date 2018/1/22
 */
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(1)
public class ThreadsTest {
  @State(Scope.Group)
  public static class FooState {
    private ConcurrentHashMap<Integer, Integer> map;
    private AtomicInteger ai = new AtomicInteger();

    @Setup
    public void setup() {
      map = new ConcurrentHashMap<>();
    }
  }

  @Group("a")
  @GroupThreads(1)
  @Benchmark
  public void put(Control ctrl, FooState state) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    state.map.put(r.nextInt(1000000), r.nextInt(1000000));
  }

  @Group("a")
  @GroupThreads(2)
  @Benchmark
  @OperationsPerInvocation(100)
  public void get(Control ctrl, FooState state, Blackhole b) {
    ThreadLocalRandom r = ThreadLocalRandom.current();
    Integer result = state.map.get(r.nextInt(1000000));
    b.consume(result);
  }
}
