package com.xzchaoo.learn.serialization.jackson.fastxmljackson.protobuf;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.math.BigDecimal;

public class BigDecimalWrapperConverter extends StdConverter<BigDecimal, BigDecimalWrapper> {
	@Override
	public BigDecimalWrapper convert(BigDecimal value) {
		String s = value.toString();
		BigDecimalWrapper bdw = new BigDecimalWrapper();
		bdw.setRaw(s);
		bdw.setLength(s.length());
		return bdw;
	}
}
