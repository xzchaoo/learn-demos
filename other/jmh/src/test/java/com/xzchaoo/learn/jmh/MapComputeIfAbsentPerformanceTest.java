package com.xzchaoo.learn.jmh;

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

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xzchaoo
 * @date 2018/5/25
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(1)
@State(Scope.Benchmark)
public class MapComputeIfAbsentPerformanceTest {
  private ConcurrentHashMap<String, String> map;
  private int n = 100_0000;

  @Setup
  public void setup() {
    Random random = new Random(1);
    map = new ConcurrentHashMap<>(n);
    for (int i = 0; i < n; ++i) {
      String key = Integer.toString(random.nextInt());
      String value = Integer.toString(random.nextInt());
      map.put(key, value);
    }
  }

  @Benchmark
  public void test_1(Blackhole b) {
    Random random = new Random(2);
    for (int i = 0; i < n; ++i) {
      String key = Integer.toString(random.nextInt());
      String value;
      value = map.get(key);
      if (value == null) {
        value = Integer.toString(random.nextInt());
        String oldValue = map.putIfAbsent(key, value);
        value = oldValue != null ? oldValue : value;
      }
      b.consume(value);
    }
  }

  @Benchmark
  public void test_2(Blackhole b) {
    Random random = new Random(2);
    for (int i = 0; i < n; ++i) {
      String key = Integer.toString(random.nextInt());
      String value = map.computeIfAbsent(key, ignore -> Integer.toString(random.nextInt()));
      b.consume(value);
    }
  }
}
