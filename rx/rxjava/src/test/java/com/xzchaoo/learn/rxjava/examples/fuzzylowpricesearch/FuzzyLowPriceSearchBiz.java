package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.val;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
public class FuzzyLowPriceSearchBiz extends AbstractFuzzyLowPriceSearchBiz {

  public FuzzyLowPriceSearchBiz(CacheProvider cacheProvider) {
    super(cacheProvider);
  }

  /**
   * 单线程版
   *
   * @param searchCriteria
   * @return
   */
  public FuzzyLowPriceSearchResult test(FuzzyLowPriceSearchCriteria searchCriteria) {
    List<FuzzyLowPriceResultItem> resultItems = null;
    switch (searchCriteria.getTripType()) {
      case OW:
      case RT:
        doSearch(searchCriteria);
        break;
      default:
        //MT暂时不支持
        throw new UnsupportedOperationException();
    }
    return FuzzyLowPriceSearchResult.builder()
      .resultItems(resultItems)
      .build();
  }


  /**
   * 时间复杂度= O(航线数 * (获取航线低价日历的代价 + 合并的代价)
   * 查询入口
   *
   * @param searchCriteria
   * @return
   */
  @Override
  @Nonnull
  protected Map<String, LowPriceCalendarData> doSearch(FuzzyLowPriceSearchCriteria searchCriteria) {
    val tripType = searchCriteria.getTripType();
    val segments = searchCriteria.getSegments().get(0);
    Map<String, LowPriceCalendarData> groupResult = new HashMap<>();

    //日期裁剪器
    Set<String> validDatePairSets = null;

    GroupMerger groupMerger = createGroupMerger(searchCriteria);

    //遍历每条航线
    for (val departureCityCode : segments.getDepartureCityCodes()) {
      for (val arrivalCityCode : segments.getArrivalCityCodes()) {
        //获取低价日历数据
        val calendar = getCityPairLowPriceCalendar(departureCityCode, arrivalCityCode, tripType, validDatePairSets);
        //合并到最终结果
        groupMerger.merge(calendar, groupResult);
      }
    }
    return groupResult;
  }

  /**
   * 读取低价日历信息
   *
   * @param departureCityCode
   * @param arrivalCityCode
   * @param tripType
   * @param validDatePairSets
   * @return
   */
  @Nonnull
  protected Map<String, LowPriceCalendarData> getCityPairLowPriceCalendar(String departureCityCode, String arrivalCityCode, TripType tripType, @Nullable Set<String> validDatePairSets) {
    //读取所有会用到的数据
    val cityPairLowPriceDatas = readAllEngineDatas(departureCityCode, arrivalCityCode, tripType, validDatePairSets);
    return mergeLowPriceCalendar(cityPairLowPriceDatas);
  }


  /**
   * 从redis里读取所有引擎的数据
   *
   * @param departureCityCode
   * @param arrivalCityCode
   * @param tripType
   * @param validDatePairSets
   * @return
   */
  private Map<String, List<LowPriceCalendarData>> readAllEngineDatas(String departureCityCode, String arrivalCityCode, TripType tripType, Set<String> validDatePairSets) {
    //拿到所有引擎
    val engines = getAllEngines();

    //同一条航线的所有数据需要自己做聚合 这点跟低价日历一样
    Map<String, List<LowPriceCalendarData>> allEngineCityPairLowPriceMap = new HashMap<>();
    for (val engine : engines) {
      val data = getEngineLowPrice(engine, tripType, departureCityCode, arrivalCityCode, validDatePairSets);
      if (data != null && data.size() > 0) {
        allEngineCityPairLowPriceMap.put(engine, data);
      }
    }
    return allEngineCityPairLowPriceMap;
  }
}

