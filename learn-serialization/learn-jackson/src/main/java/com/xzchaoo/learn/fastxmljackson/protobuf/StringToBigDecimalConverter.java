package com.xzchaoo.learn.fastxmljackson.protobuf;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.math.BigDecimal;

public class StringToBigDecimalConverter extends StdConverter<String, BigDecimal> {
	@Override
	public BigDecimal convert(String value) {
		if (value == null) {
			return null;
		}
		return new BigDecimal(value);
	}
}
