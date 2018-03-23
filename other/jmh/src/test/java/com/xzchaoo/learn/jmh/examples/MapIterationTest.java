package com.xzchaoo.learn.jmh.examples;

import com.google.common.collect.Maps;

import org.apache.commons.lang3.RandomStringUtils;
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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author zcxu
 * @date 2018/3/20 0020
 */
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(0)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
public class MapIterationTest {
  private static final int n = 10000;
  private Map<String, String> map;
  private static final int iteration = 10000;

  @Setup
  public void setup() {
    map = Maps.newHashMapWithExpectedSize(n);
    for (int i = 0; i < n; ++i) {
      String key = RandomStringUtils.random(10, true, true);
      String value = RandomStringUtils.random(10, true, true);
      map.put(key, value);
    }
  }

  /**
   * //从理论上 inner_forEach 的速度是会更快的, 因为它更靠近速度
   * //其它的调用方式都是每次遍历进进出出Map的实现
   * 内部forEach
   */
  @Benchmark
  public void test_inner_forEach(Blackhole b) {
    for (int i = 0; i < iteration; ++i) {
      int[] sum = {0};
      map.forEach((k, v) -> {
        sum[0] += k.length();
        sum[0] += v.length();
      });
      b.consume(b);
    }
  }

  @Benchmark
  public void test_inner_forEach_noValue(Blackhole b) {
    for (int i = 0; i < iteration; ++i) {
      int[] sum = {0};
      map.forEach((k, v) -> {
        sum[0] += k.length();
      });
      b.consume(b);
    }
  }

  @Benchmark
  public void test_entrySet(Blackhole b) {
    for (int i = 0; i < iteration; ++i) {
      int sum = 0;
      for (Map.Entry<String, String> e : map.entrySet()) {
        sum += e.getKey().length();
        sum += e.getValue().length();
      }
      b.consume(sum);
    }
  }

  @Benchmark
  public void test_entrySet_iter(Blackhole b) {
    for (int i = 0; i < iteration; ++i) {
      int sum = 0;
      for (Map.Entry<String, String> e : map.entrySet()) {
        sum += e.getKey().length();
        sum += e.getValue().length();
      }
      b.consume(sum);
    }
  }

  @Benchmark
  public void test_keySet(Blackhole b) {
    for (int i = 0; i < iteration; ++i) {
      int sum = 0;
      for (String key : map.keySet()) {
        sum += key.length();
        sum += map.get(key).length();
      }
      b.consume(sum);
    }
  }

  @Benchmark
  public void test_keySet_only(Blackhole b) {
    for (int i = 0; i < iteration; ++i) {
      int sum = 0;
      for (String key : map.keySet()) {
        sum += key.length();
      }
      b.consume(sum);
    }
  }

}
