package com.xzchaoo.learn.jmh.stringtointmaptest;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

/**
 * @author zcxu
 * @date 2018/1/23
 */
public class StringToIntMapTest {
  @State(Scope.Benchmark)
  public static class FooState {

    @Setup
    public void setup() {

    }
  }

  @Benchmark
  public void stringToInt(FooState state, Blackhole b) {

  }
}
