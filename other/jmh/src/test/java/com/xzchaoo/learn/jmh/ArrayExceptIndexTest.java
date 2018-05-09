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

/**
 * 在 RxJava2 的 源代码里 SingleZipArray 看到一个奇怪的用法
 * 需要遍历一个数组, 调用数组元素里的方法, 但是要忽略下标为index的元素
 * 实际测下来并没有太明显的区别
 *
 * @author xzchaoo
 * @date 2018/5/9
 */
@Warmup(time = 2, iterations = 2)
@Measurement(time = 3, iterations = 5)
@BenchmarkMode({Mode.Throughput})
@State(Scope.Benchmark)
@Fork(1)
public class ArrayExceptIndexTest {
  Integer[] array;
  final int n = 256;
  int index = 10;

  @Setup
  public void setup() {
    array = new Integer[n];
    Random random = new Random();
    for (int i = 0; i < n; ++i) {
      array[i] = random.nextInt();
    }
  }

  @Benchmark
  public void simple(Blackhole b) {
    for (int k = 0; k < 100; ++k) {
      for (int i = 0; i < array.length; ++i) {
        if (i != index) {
          b.consume(array[i].toString());
        }
      }
    }
  }

  @Benchmark
  public void simple2(Blackhole b) {
    final int n = array.length;
    for (int k = 0; k < 100; ++k) {
      for (int i = 0; i < n; ++i) {
        if (i != index) {
          b.consume(array[i].toString());
        }
      }
    }
  }

  @Benchmark
  public void better(Blackhole b) {
    final int n = array.length;
    for (int k = 0; k < 100; ++k) {
      for (int i = 0; i < index; ++i) {
        b.consume(array[i].toString());
      }
      for (int i = index + 1; i < n; ++i) {
        b.consume(array[i].toString());
      }
    }
  }
}
