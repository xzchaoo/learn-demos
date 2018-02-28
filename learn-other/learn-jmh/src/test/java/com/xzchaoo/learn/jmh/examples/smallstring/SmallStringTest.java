package com.xzchaoo.learn.jmh.examples.smallstring;

import org.apache.commons.lang3.StringUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * 对于一些长度很小的字符串, 比如长度介于0~4的大写字母组成的字符串
 * 有人认为这样的字符串的比较比一个int的比较还要浪费时间
 * 是这样的吗
 *
 * @author zcxu
 * @date 2018/2/28 0028
 */
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Fork(1)
public class SmallStringTest {

  @State(Scope.Benchmark)
  public static class FooState {
    String smallStr = "ABC";
    int aInt = 12345678;
  }

  @Benchmark
  public void test_1(FooState state, Blackhole b) {
    StringUtils.equals("a", "b");
    b.consume(state.smallStr.equals("ABD"));
  }

  @Benchmark
  public void test_2(FooState state, Blackhole b) {
    b.consume(12345679 == state.aInt);
  }
}
