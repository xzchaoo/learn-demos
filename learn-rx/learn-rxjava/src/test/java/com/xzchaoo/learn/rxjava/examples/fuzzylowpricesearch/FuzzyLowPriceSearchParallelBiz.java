package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;


import org.apache.commons.collections4.CollectionUtils;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.tuple.Tuples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import lombok.val;

/**
 * 完全并发版
 * 并发点1: 获取引擎低价时
 * 并发点2: 航线
 *
 * @author zcxu
 * @date 2018/3/1 0001
 */
public class FuzzyLowPriceSearchParallelBiz extends AbstractFuzzyLowPriceSearchBiz {
  public FuzzyLowPriceSearchParallelBiz(CacheProvider cacheProvider) {
    super(cacheProvider);
  }

  @Override
  @Nonnull
  protected Map<String, LowPriceCalendarData> doSearch(FuzzyLowPriceSearchCriteria searchCriteria) {
    val tripType = searchCriteria.getTripType();
    val segments = searchCriteria.getSegments().get(0);
    Map<String, LowPriceCalendarData> groupResult = new HashMap<>();

    //日期裁剪器
    Set<String> validDatePairSets = null;

    List<CityPair> cityPairs = new ArrayList<>(segments.getDepartureCityCodes().size() * segments.getArrivalCityCodes().size());
    for (val departureCityCode : segments.getDepartureCityCodes()) {
      for (val arrivalCityCode : segments.getArrivalCityCodes()) {
        cityPairs.add(new CityPair(departureCityCode, arrivalCityCode));
      }
    }

    GroupMerger groupMerger = createGroupMerger(searchCriteria);

    Flowable.fromIterable(cityPairs)
      //这个步骤要并发
      .flatMapSingle(cp -> getCityPairLowPriceCalendarSingle(cp.getDepartureCityCode(), cp.getArrivalCityCode(), tripType, validDatePairSets), false, 10)
      //每完成1个就可以立即合并了
      .blockingForEach(calendar -> groupMerger.merge(calendar, groupResult));

    return groupResult;
  }

  private Maybe<Pair<String, List<LowPriceCalendarData>>> getEngineLowPriceMaybe(String engine, TripType tripType, String departureCityCode, String arrivalCityCode, Set<String> validDatePairSets) {
    //Maybe的fromCallable方法是特殊的 支持返回null值
    //只有这个地方需要切换线程
    return Maybe.fromCallable(() -> {
      List<LowPriceCalendarData> engineLowPrice = getEngineLowPrice(engine, tripType, departureCityCode, arrivalCityCode, validDatePairSets);
      return CollectionUtils.isEmpty(engineLowPrice) ? null : Tuples.pair(engine, engineLowPrice);
    }).subscribeOn(Schedulers.io());
  }

  /**
   * 获取一个航线的低价日历 做成并发异步版
   *
   * @param departureCityCode
   * @param arrivalCityCode
   * @param tripType
   * @param validDatePairSets
   * @return
   */
  private Single<Map<String, LowPriceCalendarData>> getCityPairLowPriceCalendarSingle(String departureCityCode, String arrivalCityCode, TripType tripType, @Nullable Set<String> validDatePairSets) {
    //拿到所有引擎
    val engines = getAllEngines();
    return Flowable.fromIterable(engines)
      .flatMapMaybe(engine -> getEngineLowPriceMaybe(engine, tripType, departureCityCode, arrivalCityCode, validDatePairSets), false, 10)
      .toList()
      //TODO merge 的速度其实很快 有必要切到计算线程上做吗
      .observeOn(Schedulers.computation())
      .map(this::mergeLowPriceCalendar);
  }

  /**
   * 将list转成map 否则需要重写很多方法
   *
   * @param list
   * @return
   */
  private Map<String, LowPriceCalendarData> mergeLowPriceCalendar(List<Pair<String, List<LowPriceCalendarData>>> list) {
    Map<String, List<LowPriceCalendarData>> map = new HashMap<>();
    for (Pair<String, List<LowPriceCalendarData>> pair : list) {
      map.put(pair.getOne(), pair.getTwo());
    }
    return super.mergeLowPriceCalendar(map);
  }
}

