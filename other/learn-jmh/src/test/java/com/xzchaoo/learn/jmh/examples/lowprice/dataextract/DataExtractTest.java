package com.xzchaoo.learn.jmh.examples.lowprice.dataextract;

import com.google.common.collect.Maps;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 使用JMH的时候要注意JVM的优化问题, 如果JVM判断一些代码没有意义 那么就根本不会去执行它
 * <p>
 * 目前低价日历的数据时用redis的hash结构存储的 取数据的时候取出整个hash结构, 但该结果里可能会包含一些过期数据, 因此需要进行简单的过滤<br/>
 * 那么现在问题来了 哪种过滤方式效率比较高呢<br/>
 * 这里用JMH框架进行测试 只能对耗时进行测试 对内存还需要自行评估<br/>
 *
 * @author zcxu
 * @date 2018/2/28 0028
 */
@Measurement(iterations = 5)
@Warmup(iterations = 3)
@Fork(1)
public class DataExtractTest {

  private static final DateTimeFormatter yyyySMMSdd = DateTimeFormatter.ofPattern("yyyy/MM/dd");

  @State(Scope.Benchmark)
  public static class LPState {
    Map<String, String> data;

    @Setup
    public void setup() {
      data = new HashMap<>();
      LocalDate start0 = LocalDate.now().minusDays(5);
      for (int i = 0; i < 55; ++i) {
        LocalDate out = start0.plusDays(i);
        String prefix = yyyySMMSdd.format(out) + "-";
        for (int j = 1; j < 10; ++j) {
          LocalDate in = out.plusDays(j);
          String key = prefix + yyyySMMSdd.format(in);
          data.put(key, "");
        }
      }
    }
  }

  /**
   * 直观感受是该方法比较快 事实也确实如此
   *
   * @param state
   * @param b
   */
  @Benchmark
  public void test_1(LPState state, Blackhole b) {
    //有一个优化的点 既然每次生成的 key 都是固定的 那么可以先将keys都存下来, 下次直接用这个key数组, 不用每次去重新生成
    //当过了一天之后需要重新生成该序列
    Map<String, String> data = state.data;
    Map<String, String> newData = Maps.newHashMapWithExpectedSize(45 * 7);
    LocalDate today = LocalDate.now();
    for (int i = 0; i < 45; ++i) {
      LocalDate out = today.plusDays(i);
      String prefix = yyyySMMSdd.format(out) + "-";
      for (int j = 1; j <= 7; ++j) {
        LocalDate in = out.plusDays(j);
        String key = prefix + yyyySMMSdd.format(in);
        String value = data.get(key);
        if (value != null) {
          newData.put(key, value);
        }
      }
    }
    b.consume(newData);
  }

  @Benchmark
  public void test_2(LPState state, Blackhole b) {
    Map<String, String> data = state.data;
    Map<String, String> newData = Maps.newHashMapWithExpectedSize(45 * 7);
    LocalDate today = LocalDate.now();
    for (String key : data.keySet()) {
      int index = key.indexOf('-');
      LocalDate out = LocalDate.parse(key.substring(0, index), yyyySMMSdd);
      int days = (int) ChronoUnit.DAYS.between(today, out);
      if (days < 0 || days >= 45) {
        continue;
      }
      LocalDate in = LocalDate.parse(key.substring(index + 1), yyyySMMSdd);
      days = (int) ChronoUnit.DAYS.between(out, in);
      if (days < 1 || days > 7) {
        continue;
      }
      newData.put(key, data.get(key));
    }
    b.consume(newData);
  }
}
