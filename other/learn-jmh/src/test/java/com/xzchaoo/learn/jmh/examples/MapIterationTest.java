package com.xzchaoo.learn.jmh.examples;

import com.google.common.collect.Maps;

import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Map;

/**
 * @author zcxu
 * @date 2018/3/20 0020
 */
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(0)
@State(Scope.Benchmark)
public class MapIterationTest {
  int n = 10000;
  Map<String, String> map;

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
    int[] sum = {0};
    map.forEach((k, v) -> {
      sum[0] += k.length();
      sum[0] += v.length();
    });
    b.consume(b);
  }
  @Benchmark
  public void test_inner_forEach2(Blackhole b) {
    int[] sum = {0};
    map.forEach((k, v) -> {
      sum[0] += k.length();
      //sum[0] += v.length();
    });
    b.consume(b);
  }

  @Benchmark
  public void test_entrySet(Blackhole b) {
    int sum = 0;
    for (Map.Entry<String, String> e : map.entrySet()) {
      sum += e.getKey().length();
      sum += e.getValue().length();
    }
    b.consume(sum);
  }

  @Benchmark
  public void test_keySet(Blackhole b) {
    int sum = 0;
    for (String key : map.keySet()) {
      sum += key.length();
      sum += map.get(key).length();
    }
    b.consume(sum);
  }

  @Benchmark
  public void test_keySet_only(Blackhole b) {
    int sum = 0;
    for (String key : map.keySet()) {
      sum += key.length();
    }
    b.consume(sum);
  }

  @Benchmark
  public void test_values_only(Blackhole b) {
    int sum = 0;
    for (String value : map.values()) {
      sum += value.length();
    }
    b.consume(sum);
  }
}
