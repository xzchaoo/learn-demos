package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import org.apache.commons.collections4.MapUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lombok.val;

/**
 * 提供一些基础方法
 *
 * @author zcxu
 * @date 2018/3/1 0001
 */
public abstract class AbstractFuzzyLowPriceSearchBiz {
  private CacheProvider cacheProvider;

  public AbstractFuzzyLowPriceSearchBiz(CacheProvider cacheProvider) {
    Preconditions.checkNotNull(cacheProvider);
    this.cacheProvider = cacheProvider;
  }

  /**
   * 获取所有引擎
   *
   * @return
   */
  protected Set<String> getAllEngines() {
    return Sets.newHashSet("Ctrip", "Spring");
  }

  private Set<String> getBasicEngines() {
    return Sets.newHashSet("Ctrip");
  }

  /**
   * 由子类实现的抽象方法
   *
   * @param searchCriteria
   * @return
   */
  @Nonnull
  protected abstract Map<String, LowPriceCalendarData> doSearch(FuzzyLowPriceSearchCriteria searchCriteria);

  public List<FuzzyLowPriceResultItem> search(FuzzyLowPriceSearchCriteria searchCriteria) {
    switch (searchCriteria.getTripType()) {
      case OW:
      case RT:
        return searchOWRT(searchCriteria);
      default:
        //MT还不支持
        throw new UnsupportedOperationException();
    }
  }

  @Nonnull
  private List<FuzzyLowPriceResultItem> searchOWRT(FuzzyLowPriceSearchCriteria searchCriteria) {
    Map<String, LowPriceCalendarData> result = doSearch(searchCriteria);
    if (MapUtils.isEmpty(result)) {
      return Collections.emptyList();
    }
    List<FuzzyLowPriceResultItem> ret = new ArrayList<>(result.size());
    PriceType priceType = searchCriteria.getPriceType();
    int segmentSize = searchCriteria.getTripType() == TripType.OW ? 1 : 2;
    for (LowPriceCalendarData data : result.values()) {
      FuzzyLowPriceResultItem item = new FuzzyLowPriceResultItem();
      List<FuzzyLowPriceResponseSegment> segments = new ArrayList<>(segmentSize);
      segments.add(new FuzzyLowPriceResponseSegment(data.getDepartureCityCode(), data.getArrivalCityCode(), data.getOutboundDate()));
      if (data.getInboundDate() != null) {
        segments.add(new FuzzyLowPriceResponseSegment(data.getArrivalCityCode(), data.getDepartureCityCode(), data.getInboundDate()));
      }
      item.setSegments(segments);
      item.setValue(priceType == PriceType.SalesPrice ? data.getSalesPrice() : data.getTotalPrice());
    }
    return ret;
  }

  @Nonnull
  private static List<LowPriceCalendarData> convertAll(Map<String, String> rawData, LocalDate minOutboundDate) {
    List<LowPriceCalendarData> list = new ArrayList<>(rawData.size());
    for (Map.Entry<String, String> e : rawData.entrySet()) {
      LowPriceCalendarData data = deserializeData(e.getValue());
      if (data != null) {
        data.setDatePairKey(e.getKey());
        if (data.getOutboundDate().isBefore(minOutboundDate)) {
          continue;
        }
        list.add(data);
      }
    }
    return list;
  }

  @Nonnull
  private static List<LowPriceCalendarData> convertPartial(Map<String, String> rawData, Set<String> validDatePairSets) {
    List<LowPriceCalendarData> list = new ArrayList<>(validDatePairSets.size());
    for (String datePairKey : validDatePairSets) {
      String value = rawData.get(datePairKey);
      if (value != null) {
        LowPriceCalendarData data = deserializeData(value);
        if (data != null) {
          data.setDatePairKey(datePairKey);
          list.add(data);
        }
      }
    }
    return list;
  }

  /**
   * 反序列化回我们的model
   *
   * @param rawData
   * @return
   */
  private static LowPriceCalendarData deserializeData(String rawData) {
    LowPriceCalendarData data = new LowPriceCalendarData();
    data.setOutboundDate(LocalDate.now());
    data.setInboundDate(LocalDate.now());
    data.setSalesPrice(0);
    data.setTotalPrice(0);
    data.setNonStopSalesPrice(0);
    data.setNonStopTotalPrice(0);
    data.setTimestamp(0);
    return data;
  }

  /**
   * 从redis读取引擎低价数据
   *
   * @param engine
   * @param tripType
   * @param departureCityCode
   * @param arrivalCityCode
   * @return
   */
  @Nullable
  protected List<LowPriceCalendarData> getEngineLowPrice(String engine, TripType tripType, String departureCityCode, String arrivalCityCode, Set<String> validDatePairSets) {
    //构造key
    String key = engine + ":" + tripType + ":" + departureCityCode + "-" + arrivalCityCode;
    //是否 partial 读取
    boolean readPartial = validDatePairSets != null && validDatePairSets.size() < 100;
    Map<String, String> rawData;
    if (readPartial) {
      rawData = cacheProvider.hmget(key, validDatePairSets.toArray(new String[0]));
    } else {
      rawData = cacheProvider.hgetall(key);
    }
    if (rawData == null || rawData.size() == 0) {
      return null;
    }
    List<LowPriceCalendarData> list;
    if (validDatePairSets == null || !readPartial) {
      list = convertAll(rawData, LocalDate.now());
    } else {
      list = convertPartial(rawData, validDatePairSets);
    }
    for (LowPriceCalendarData d : list) {
      //填充城市信息
      d.setDepartureCityCode(departureCityCode);
      d.setArrivalCityCode(arrivalCityCode);
    }
    return list;
  }

  private List<Map<String, Long>> getTimestamp(Map<String, List<LowPriceCalendarData>> cityPairLowPriceDatas) {
    //非补偿引擎
    val basicEngines = getBasicEngines();
    //遍历一次 保存每个日期对的最新的时间戳
    Map<String, Long> basicTimestampMap = new HashMap<>();
    Map<String, Long> compTimestampMap = new HashMap<>();

    for (Map.Entry<String, List<LowPriceCalendarData>> e : cityPairLowPriceDatas.entrySet()) {
      for (LowPriceCalendarData datePairAndData : e.getValue()) {
        Map<String, Long> timestampMap = basicEngines.contains(e.getKey()) ? basicTimestampMap : compTimestampMap;
        long dataTimestamp = datePairAndData.getTimestamp();
        Long newestTimestamp = timestampMap.get(datePairAndData.getDatePairKey());
        if (newestTimestamp == null || dataTimestamp > newestTimestamp) {
          timestampMap.put(datePairAndData.getDatePairKey(), dataTimestamp);
        }
      }
    }
    return Arrays.asList(basicTimestampMap, compTimestampMap);
  }

  /**
   * RT要稍微特殊处理一下
   *
   * @param searchCriteria
   */
  private void validateRT(FuzzyLowPriceSearchCriteria searchCriteria) {
    List<FuzzyLowPriceRequestSegment> segments = searchCriteria.getSegments();
    Sets.SetView<String> d = Sets.intersection(segments.get(0).getDepartureCityCodes(), segments.get(1).getArrivalCityCodes());
    Sets.SetView<String> a = Sets.intersection(segments.get(0).getArrivalCityCodes(), segments.get(1).getDepartureCityCodes());
  }

  protected Map<String, LowPriceCalendarData> mergeLowPriceCalendar(Map<String, List<LowPriceCalendarData>> cityPairLowPriceDatas) {
    //用于存储合并结果
    Map<String, LowPriceCalendarData> merged = new HashMap<>();

    //保存每个日期对的最新的时间戳
    val newestTimestampMaps = getTimestamp(cityPairLowPriceDatas);
    val basicTimestampMap = newestTimestampMaps.get(0);
    val compTimestampMap = newestTimestampMaps.get(1);


    //TODO comp太旧问题(使用另外一种时间戳)

    //非补偿引擎
    val basicEngines = getBasicEngines();

    //TODO 读取配置
    val dataTtlMills = 86400L * 1000 * 100;
    val sameTypeMills = 600L * 1000;

    val now = System.currentTimeMillis();

    for (val e : cityPairLowPriceDatas.entrySet()) {
      val timestampMap = basicEngines.contains(e.getKey()) ? basicTimestampMap : compTimestampMap;
      for (val data : e.getValue()) {
        //该数据已经过期
        if (data.getTimestamp() + dataTtlMills < now) {
          continue;
        }

        //获取这个日期对的最新的时间戳, 根据逻辑 肯定非null
        val datePairKey = data.getDatePairKey();
        val newestTimestamp = timestampMap.get(datePairKey);

        //如果当前数据比同类型的最新数据要旧 那么就忽略它, 是否需要顺便反向删除?
        if (data.getTimestamp() + sameTypeMills < newestTimestamp) {
          continue;
        }

        val data2 = merged.get(datePairKey);
        if (data2 == null || data.getTotalPrice() < data2.getTotalPrice()) {
          merged.put(datePairKey, data);
        }
      }

    }

    return merged;
  }


  protected GroupMerger createGroupMerger(FuzzyLowPriceSearchCriteria searchCriteria) {
    return GroupMergerBuilder.create()
      .sort(searchCriteria.getPriceType() == PriceType.SalesPrice ? GroupMergerBuilder.COMPARED_BY_SALESPRICE : GroupMergerBuilder.COMPARED_BY_TOTALPRICE)
      .groupBy(searchCriteria.getGroupMode() == GroupMode.CITY_PAIR ? GroupMergerBuilder.GROUP_BY_CITYPAIR : GroupMergerBuilder.GROUP_BY_DATEPAIR)
      .build();
  }
}
