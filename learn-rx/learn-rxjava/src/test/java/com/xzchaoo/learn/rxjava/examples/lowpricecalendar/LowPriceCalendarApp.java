package com.xzchaoo.learn.rxjava.examples.lowpricecalendar;

import com.google.common.collect.Maps;

import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

/**
 * 低价日历按照 key=Ctrip:OW:SHA-HKG field=2017/01/01 value={PB序列化的value} 在redis里用hash结构存储
 *
 * @author zcxu
 * @date 2018/2/11 0011
 */
public class LowPriceCalendarApp {
  private static final DateTimeFormatter yyyySMMSdd = DateTimeFormatter.ofPattern("yyyy/MM/dd");

  /**
   * 单线程版低价日历
   */
  @Test
  public void test_singleThread() {
    CacheProvider cp = null;
    String tripType = "RT";
    String departureCityCode = "SHA";
    String arrivalCityCode = "HKG";
    LocalDate today = LocalDate.now();
    List<String> engines = new ArrayList<>();
    //TODO预估大小
    Map<String, String> finalResult = new HashMap<>();
    boolean ow = "OW".equals(tripType);
    for (String engine : engines) {
      String key = engine + ":" + tripType + ":" + departureCityCode + "-" + arrivalCityCode;
      Map<String, String> result = cp.hgetall(key);
      if (result == null && result.size() == 0) {
        continue;
      }
      result = ow ? extractOW(result) : extractRT1(result);
      merge(finalResult, result);
    }
  }

  @Test
  public void test_multiThread() {
    CacheProvider cp = null;
    String tripType = "RT";
    String departureCityCode = "SHA";
    String arrivalCityCode = "HKG";
    List<String> engines = new ArrayList<>();

    boolean ow = "OW".equals(tripType);
    Flowable.fromIterable(engines)
      .flatMapMaybe(engine -> {
        String key = engine + ":" + tripType + ":" + departureCityCode + "-" + arrivalCityCode;
        Map<String, String> result = cp.hgetall(key);
        if (result == null && result.size() == 0) {
          return Maybe.empty();
        }
        result = ow ? extractOW(result) : extractRT1(result);
        return Maybe.just(result);
      }).reduceWith(() -> new HashMap<String, String>(/*TODO 预估大小*/), (finalResult, result) -> {
      merge(finalResult, result);
      return finalResult;
    }).blockingGet();
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

  private void merge(Map<String, String> finalResult, Map<String, String> engineResult) {
    for (Map.Entry<String, String> e : engineResult.entrySet()) {
      String oldValue = finalResult.get(e.getKey());
      boolean useNew = false;
      if (useNew) {
        finalResult.put(e.getKey(), e.getValue());
      }
    }
  }

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