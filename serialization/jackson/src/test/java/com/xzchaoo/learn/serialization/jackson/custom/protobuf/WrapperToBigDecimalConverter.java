package com.xzchaoo.learn.serialization.jackson.custom.protobuf;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.math.BigDecimal;

public class WrapperToBigDecimalConverter extends StdConverter<BigDecimalWrapper, BigDecimal> {
  @Override
  public BigDecimal convert(BigDecimalWrapper value) {
    return new BigDecimal(value.getRaw());
  }
}
