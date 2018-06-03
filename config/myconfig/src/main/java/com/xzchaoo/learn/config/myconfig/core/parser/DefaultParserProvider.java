package com.xzchaoo.learn.config.myconfig.core.parser;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.BitSet;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.bind.DatatypeConverter;

/**
 * @author xzchaoo
 * @date 2018/6/1
 */
public class DefaultParserProvider implements ParserProvider {
  private static final ConcurrentHashMap<Class, Parser<?>> PROPERTY_PARSER_CACHE = new ConcurrentHashMap<>();
  /**
   * TODO 分离出去
   * 存放默认的 valueParser
   */
  private static final Map<Class<?>, Parser<?>> DEFAULT_PARSER;

  static {
    Map<Class<?>, Parser<?>> defaultParserMap = new IdentityHashMap<>();
    defaultParserMap.put(String.class, x -> x);

    defaultParserMap.put(int.class, Integer::valueOf);
    defaultParserMap.put(Integer.class, Integer::valueOf);

    defaultParserMap.put(float.class, Float::valueOf);
    defaultParserMap.put(Float.class, Float::valueOf);

    defaultParserMap.put(double.class, Double::valueOf);
    defaultParserMap.put(Double.class, Double::valueOf);

    defaultParserMap.put(long.class, Long::valueOf);
    defaultParserMap.put(Long.class, Long::valueOf);

    defaultParserMap.put(boolean.class, Boolean::valueOf);
    defaultParserMap.put(Boolean.class, Boolean::valueOf);

    // 其它稍微复杂一些的类型如果不需要可以注释掉
    defaultParserMap.put(BigInteger.class, BigInteger::new);
    defaultParserMap.put(BigDecimal.class, BigDecimal::new);
    defaultParserMap.put(AtomicInteger.class, s -> new AtomicInteger(Integer.parseInt(s)));
    defaultParserMap.put(AtomicLong.class, s -> new AtomicLong(Long.parseLong(s)));
    defaultParserMap.put(Duration.class, Duration::parse);
    defaultParserMap.put(Period.class, Period::parse);
    defaultParserMap.put(LocalDateTime.class, LocalDateTime::parse);
    defaultParserMap.put(LocalDate.class, LocalDate::parse);
    defaultParserMap.put(LocalTime.class, LocalTime::parse);
    defaultParserMap.put(OffsetDateTime.class, OffsetDateTime::parse);
    defaultParserMap.put(OffsetTime.class, OffsetTime::parse);
    defaultParserMap.put(ZonedDateTime.class, ZonedDateTime::parse);
    defaultParserMap.put(Instant.class, v -> Instant.from(OffsetDateTime.parse(v)));
    defaultParserMap.put(Date.class, v -> new Date(Long.parseLong(v)));
    defaultParserMap.put(Currency.class, Currency::getInstance);
    defaultParserMap.put(BitSet.class, v -> BitSet.valueOf(DatatypeConverter.parseHexBinary(v)));

    DEFAULT_PARSER = Collections.unmodifiableMap(defaultParserMap);
  }

  @SuppressWarnings("unchecked")
  private static <T> Parser<T> getOrCreateParser(Class<? extends Parser<? extends T>> parserClass) {
    return (Parser<T>) PROPERTY_PARSER_CACHE.computeIfAbsent(parserClass, clazz2 -> {
      try {
        return (Parser<?>) clazz2.newInstance();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private final Map<Class<?>, Parser<?>> parserMap;

  public DefaultParserProvider() {
    this(null);
  }

  public DefaultParserProvider(Map<Class<?>, Parser<?>> customParserMap) {
    if (customParserMap == null || customParserMap.isEmpty()) {
      parserMap = DEFAULT_PARSER;
    } else {
      Map<Class<?>, Parser<?>> map = new HashMap<>(DEFAULT_PARSER);
      map.putAll(customParserMap);
      parserMap = Collections.unmodifiableMap(map);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> Parser<T> getParser(Class<T> clazz) {
    return (Parser<T>) parserMap.get(clazz);
  }

  @Override
  public <T> Parser<T> getParser(Class<T> clazz, Class<? extends Parser<? extends T>> defaultParserClass) {
    Parser<T> parser = getParser(clazz);
    // noinspection RedundantCast
    if (parser == null && (Class) defaultParserClass != Parser.None.class) {
      parser = getOrCreateParser(defaultParserClass);
    }
    return parser;
  }
}
