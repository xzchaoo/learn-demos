package com.xzchaoo.learn.serialization.jackson.date;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

/**
 * created by xzchaoo at 2017/12/5
 *
 * @author xzchaoo
 */
public class DateTest {
	private ObjectMapper om;

	@Before
	public void before() {
		om = new ObjectMapper();
		//默认情况下 jackson 不支持 jdk7 jdk8 引进的类, 比如 LocalDate Path 等, 需要手动注册
		om.registerModule(new JavaTimeModule());

		//默认的策略是将日期写成时间戳 这会导致 localDate 序列化成一个数组...
		om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		//默认 反序列化遇到未知的属性就会fail, 通常要禁止掉
		om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		//默认是false 所以没必要disable 基础数据类型遇到null则fail
		//om.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
	}

	@Test
	public void test1() throws JsonProcessingException {
		ObjectWithDate owd = new ObjectWithDate();
		owd.setDate(new Date());
		owd.setLocalDate(LocalDate.now());
		owd.setLocalDateTime(LocalDateTime.now());
		owd.setZonedDateTime(ZonedDateTime.now());
		owd.setTimestamp(123);
		owd.setCalendar(Calendar.getInstance());
		String s = om.writeValueAsString(owd);
		System.out.println(s);
	}
}
