package com.xzchaoo.learn.jdk8.time;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2017/3/21.
 */
public class TimeApp {
	@Test
	public void test_parse() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-d");
		System.out.println(dtf.parse("2017-11-23", LocalDate::from));
	}

	@Test
	public void test_LocalTime() {
		LocalTime parse = LocalTime.parse("00:06:00");
		System.out.println(parse);
	}

	@Test
	public void test_mills_to_LocalDateTime() {
		long mills = 1;
		System.out.println(LocalDateTime.ofInstant(Instant.ofEpochMilli(mills), ZoneId.systemDefault()));
	}

	@Test
	public void test_mills_to_LocalDate() {
		long mills = 1;
		System.out.println(LocalDateTime.ofInstant(Instant.ofEpochMilli(mills), ZoneId.systemDefault()).toLocalDate());
	}

	@Test
	public void test1() {
		//System.out.println(DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.JAPANESE).format(0L));
		//1970年1月1日 8時00分00秒 CST

		//本地日期
		//TimeZone sh = TimeZone.getTimeZone("");
		ZoneId sh = ZoneId.of("Asia/Shanghai");
		long mills = 1490080296204L;
		Instant instant = Instant.ofEpochMilli(mills);

		LocalDate ld = LocalDate.now(Clock.fixed(instant, sh));

		assertEquals(2017, ld.getYear());
		assertEquals(3, ld.getMonthValue());
		assertEquals(21, ld.getDayOfMonth());

		LocalDateTime ldt = ld.atStartOfDay();
		assertEquals(2017, ldt.getYear());
		assertEquals(3, ldt.getMonthValue());
		assertEquals(21, ldt.getDayOfMonth());
		assertEquals(0, ldt.getHour());
		assertEquals(0, ldt.getMinute());
		assertEquals(0, ldt.getSecond());

		ldt = LocalDateTime.ofInstant(instant, sh);
		assertEquals(2017, ldt.getYear());
		assertEquals(3, ldt.getMonthValue());
		assertEquals(21, ldt.getDayOfMonth());
		assertEquals(15, ldt.getHour());
		assertEquals(11, ldt.getMinute());
		assertEquals(36, ldt.getSecond());

		assertEquals("20170321 151136", ldt.format(DateTimeFormatter.ofPattern("yyyyMMdd HHmmss")));

	}
}
