package com.xzhcaoo.learn;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalDate;

/**
 * @author zcxu
 * @date 2017/12/21
 */
@Deprecated
public class LocalDateToStringConverter extends StdConverter<LocalDate, String> {
	@Override
	public String convert(LocalDate value) {
		return value.toString();
	}
}
