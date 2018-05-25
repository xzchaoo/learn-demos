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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author xzchaoo
 * @date 2018/5/24
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 2)
@Measurement(iterations = 5)
@State(Scope.Benchmark)
@Fork(1)
public class BranchPredicateTest {
  private final int n = 100_0000;
  private List<Integer> list1;
  private List<Integer> list2;
  private List<Integer> list3;
  private int mid;

  @Setup
  public void setup() {
    Random random = new Random();
    list1 = new ArrayList<>(n);
    list2 = new ArrayList<>(n);
    list3 = new ArrayList<>(n);
    for (int i = 0; i < n; ++i) {
      int x = random.nextInt() & 0X7FFFFFFF;
      list1.add(x);
    }
    list2.addAll(list1);
    list3.addAll(list1);
    Collections.shuffle(list1);

    //list2 = new ArrayList<>(list1);
    list2.sort(Integer::compare);
    mid = list2.get(n - 1);

    //list3 = new ArrayList<>(list1);
    list3.sort((a, b) -> Integer.compare(b, a));
//
//    int sum = 0;
//    for (int i = 0; i < n; ++i) {
//      sum += list1.get(i);
//      sum += list2.get(i);
//      sum += list3.get(i);
//    }
//    System.out.println(sum);
  }

  @Benchmark
  public void test_1(Blackhole b) {
    int mid = this.mid;
    List<Integer> list = this.list1;
    double count = 0;
    for (int i = 0; i < n; ++i) {
      Integer x = list.get(i);
      if (x < mid) {
        count += Math.log(x);
      }
    }
    b.consume(count);
  }

  @Benchmark
  public void test_2(Blackhole b) {
    int mid = this.mid;
    List<Integer> list = this.list2;
    double count = 0;
    for (int i = 0; i < n; ++i) {
      Integer x = list.get(i);
      if (x < mid) {
        count += Math.log(x);
      }
    }
    b.consume(count);
  }

  @Benchmark
  public void test_3(Blackhole b) {
    int mid = this.mid;
    List<Integer> list = this.list3;
    double count = 0;
    for (int i = 0; i < n; ++i) {
      Integer x = list.get(i);
      if (x < mid) {
        count += Math.log(x);
      }
    }
    b.consume(count);
  }

}
