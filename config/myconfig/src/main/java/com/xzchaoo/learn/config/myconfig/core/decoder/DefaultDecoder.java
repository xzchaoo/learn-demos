package com.xzchaoo.learn.config.myconfig.core.decoder;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
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
import java.util.Currency;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import javax.xml.bind.DatatypeConverter;

/**
 * @author xzchaoo
 * @date 2018/6/2
 */
public class DefaultDecoder implements Decoder {
  private Map<Class<?>, Function<String, ?>> decoderRegistry;

  public DefaultDecoder() {
    decoderRegistry = new IdentityHashMap<>(75);
    decoderRegistry.put(String.class, v -> v);
    decoderRegistry.put(boolean.class, v -> {
      if (v.equalsIgnoreCase("true") || v.equalsIgnoreCase("yes") || v.equalsIgnoreCase("on")) {
        return Boolean.TRUE;
      } else if (v.equalsIgnoreCase("false") || v.equalsIgnoreCase("no") || v.equalsIgnoreCase("off")) {
        return Boolean.FALSE;
      }
      throw new IllegalArgumentException("Error parsing value '" + v + "'", new Exception("Expected one of [true, yes, on, false, no, off]"));

    });
    decoderRegistry.put(Boolean.class, decoderRegistry.get(boolean.class));
    decoderRegistry.put(Integer.class, Integer::valueOf);
    decoderRegistry.put(int.class, Integer::valueOf);
    decoderRegistry.put(long.class, Long::valueOf);
    decoderRegistry.put(Long.class, Long::valueOf);
    decoderRegistry.put(short.class, Short::valueOf);
    decoderRegistry.put(Short.class, Short::valueOf);
    decoderRegistry.put(byte.class, Byte::valueOf);
    decoderRegistry.put(Byte.class, Byte::valueOf);
    decoderRegistry.put(double.class, Double::valueOf);
    decoderRegistry.put(Double.class, Double::valueOf);
    decoderRegistry.put(float.class, Float::valueOf);
    decoderRegistry.put(Float.class, Float::valueOf);
    decoderRegistry.put(BigInteger.class, BigInteger::new);
    decoderRegistry.put(BigDecimal.class, BigDecimal::new);
    decoderRegistry.put(AtomicInteger.class, s -> new AtomicInteger(Integer.parseInt(s)));
    decoderRegistry.put(AtomicLong.class, s -> new AtomicLong(Long.parseLong(s)));
    decoderRegistry.put(Duration.class, Duration::parse);
    decoderRegistry.put(Period.class, Period::parse);
    decoderRegistry.put(LocalDateTime.class, LocalDateTime::parse);
    decoderRegistry.put(LocalDate.class, LocalDate::parse);
    decoderRegistry.put(LocalTime.class, LocalTime::parse);
    decoderRegistry.put(OffsetDateTime.class, OffsetDateTime::parse);
    decoderRegistry.put(OffsetTime.class, OffsetTime::parse);
    decoderRegistry.put(ZonedDateTime.class, ZonedDateTime::parse);
    decoderRegistry.put(Instant.class, v -> Instant.from(OffsetDateTime.parse(v)));
    decoderRegistry.put(Date.class, v -> new Date(Long.parseLong(v)));
    decoderRegistry.put(Currency.class, Currency::getInstance);
    decoderRegistry.put(BitSet.class, v -> BitSet.valueOf(DatatypeConverter.parseHexBinary(v)));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T decode(Class<T> clazz, String str) {
    if (str == null) {
      return null;
    }
    Function<String, ?> decoderFunc = decoderRegistry.get(clazz);
    if (decoderFunc != null) {
      return (T) decoderFunc.apply(str);
    }
    if (clazz.isArray()) {
      String[] ss = StringUtils.split(str, ',');
      int n = ss.length;
      Class<?> componentType = clazz.getComponentType();
      Object[] array = (Object[]) Array.newInstance(componentType, n);
      for (int i = 0; i < n; ++i) {
        array[i] = decode(componentType, ss[i]);
      }
      return (T) array;
    }
    throw new RuntimeException("Unable to decoder " + str + " for class " + clazz);
  }
}
