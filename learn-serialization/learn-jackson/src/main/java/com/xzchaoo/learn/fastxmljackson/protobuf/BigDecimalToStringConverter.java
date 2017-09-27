package com.xzchaoo.learn.fastxmljackson.protobuf;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.math.BigDecimal;

public class BigDecimalToStringConverter extends StdConverter<BigDecimal, String> {
	@Override
	public String convert(BigDecimal value) {
		return value == null ? null : value.toString();
	}
}
