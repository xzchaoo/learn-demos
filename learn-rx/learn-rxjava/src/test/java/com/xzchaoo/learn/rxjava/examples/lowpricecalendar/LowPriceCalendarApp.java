package com.xzchaoo.learn.rxjava.examples.lowpricecalendar;

import com.google.common.collect.Maps;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;

/**
 * 低价日历按照 key=Ctrip:OW:SHA-HKG field=2017/01/01 value={PB序列化的value} 在redis里用hash结构存储
 *
 * @author zcxu
 * @date 2018/2/11 0011
 */
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(1)
public class LowPriceCalendarApp {
  private static final DateTimeFormatter yyyySMMSdd = DateTimeFormatter.ofPattern("yyyy/MM/dd");

  @State(Scope.Benchmark)
  public static class FooState {
    List<String> engines = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H");
    CacheProvider cacheProvider = new CacheProvider() {
      @Override
      public Map<String, String> hgetall(String key) {
        Map<String, String> map = new HashMap<>();
        map.put("2017/01/01", "b");
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        return map;
      }
    };
  }

  /**
   * 单线程版低价日历
   */
  @Benchmark
  public void test_singleThread(FooState state, Blackhole b) {
    String tripType = "RT";
    String departureCityCode = "SHA";
    String arrivalCityCode = "HKG";
    List<String> engines = state.engines;
    CacheProvider cp = state.cacheProvider;
    Map<String, String> finalResult = new HashMap<>();
    for (String engine : engines) {
      Map<String, String> result = search(cp, engine, tripType, departureCityCode, arrivalCityCode);
      merge(finalResult, result);
    }
    b.consume(finalResult);
  }

  @Benchmark
  public void test_multiThread(FooState state, Blackhole b) {
    //这里直接写成一个方法了 如果拆成独立方法会更简洁一些

    String tripType = "RT";
    String departureCityCode = "SHA";
    String arrivalCityCode = "HKG";
    List<String> engines = state.engines;
    CacheProvider cp = state.cacheProvider;

    Map<String, String> finalResult = Flowable.fromIterable(engines)
      .flatMapMaybe(engine -> searchMaybe(cp, engine, tripType, departureCityCode, arrivalCityCode))
      .reduceWith(HashMap::new, this::merge)
      .blockingGet();
    b.consume(finalResult);
  }

  private Map<String, String> search(CacheProvider cacheProvider, String engine, String tripType, String departureCityCode, String arrivalCityCode) {
    boolean ow = "OW".equals(tripType);
    String key = engine + ":" + tripType + ":" + departureCityCode + "-" + arrivalCityCode;
    Map<String, String> result = cacheProvider.hgetall(key);
    if (result == null && result.size() == 0) {
      return null;
    }
    result = ow ? extractOW(result) : extractRT1(result);
    return result;
  }

  private Maybe<Map<String, String>> searchMaybe(CacheProvider cacheProvider, String engine, String tripType, String departureCityCode, String arrivalCityCode) {
    return Maybe.<Map<String, String>>create(emitter -> {
      Map<String, String> result = search(cacheProvider, engine, tripType, departureCityCode, arrivalCityCode);
      if (result != null) {
        emitter.onSuccess(result);
      } else {
        emitter.onComplete();
      }
    }).subscribeOn(Schedulers.io());
  }

  private Map<String, String> merge(Map<String, String> finalResult, Map<String, String> engineResult) {
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    for (Map.Entry<String, String> e : engineResult.entrySet()) {
      String oldValue = finalResult.get(e.getKey());
      boolean useNew = false;
      if (useNew) {
        finalResult.put(e.getKey(), e.getValue());
      }
    }
    return finalResult;
  }

  private Map<String, String> extractOW(Map<String, String> result) {
    LocalDate today = LocalDate.now();
    Map<String, String> ret = Maps.newHashMapWithExpectedSize(Math.min(90, result.size()));
    for (int i = 0; i < 90; ++i) {
      LocalDate out = today.plusDays(i);
      String outStr = yyyySMMSdd.format(out);
      String value = result.get(outStr);
      if (value != null) {
        ret.put(outStr, value);
      }
    }
    return ret;
  }

  /**
   * 遍历合法的区间将数据取出来
   *
   * @param result
   * @return
   */
  private Map<String, String> extractRT1(Map<String, String> result) {
    LocalDate today = LocalDate.now();
    Map<String, String> ret = Maps.newHashMapWithExpectedSize(Math.min(45 * 7, result.size()));
    for (int i = 0; i < 45; ++i) {
      LocalDate out = today.plusDays(i);
      String prefix = yyyySMMSdd.format(out) + "-";
      for (int j = 1; j <= 7; ++j) {
        LocalDate in = out.plusDays(j);
        String field = prefix + yyyySMMSdd.format(in);
        String value = result.get(field);
        if (value != null) {
          ret.put(field, value);
        }
      }
    }
    return ret;
  }

  /**
   * 遍历所有结果 检查它是否在合法区间里
   *
   * @param result
   * @return
   */
  private Map<String, String> extractRT2(Map<String, String> result) {
    LocalDate today = LocalDate.now();
    Map<String, String> ret = Maps.newHashMapWithExpectedSize(Math.min(45 * 7, result.size()));
    for (Map.Entry<String, String> e : result.entrySet()) {
      String field = e.getKey();
      boolean valid = true;
      int index = field.indexOf('-');
      String outStr = field.substring(0, index);
      LocalDate out = yyyySMMSdd.parse(outStr, LocalDate::from);
      long outOffset = ChronoUnit.DAYS.between(today, out);
      //> or >=
      if (outOffset < 0 || outOffset > 90) {
        valid = false;
      } else {
        String inStr = field.substring(index + 1);
        LocalDate in = LocalDate.parse(inStr, yyyySMMSdd);
        long days = ChronoUnit.DAYS.between(out, in);
        valid = days >= 1 && days <= 7;
      }
      if (valid) {
        ret.put(e.getKey(), e.getValue());
      }
    }
    return ret;
  }
}