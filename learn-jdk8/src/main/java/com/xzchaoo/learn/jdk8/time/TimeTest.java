package com.xzchaoo.learn.jdk8.time;

import org.junit.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/3/21.
 */
public class TimeTest {
	@Test
	public void test_mills() {
		System.out.println(LocalDateTime.of(1, 1, 1, 1, 1, 1).plus(1, ChronoUnit.MILLIS));
	}

	@Test
	public void test() {
		ZoneId usChicago = ZoneId.of("America/Chicago");
// 2013-03-10T02:30 did not exist in America/Chicago time zone
		LocalDateTime ldt = LocalDateTime.of(2013, Month.MARCH, 10, 2, 30);
		ZonedDateTime zdt = ZonedDateTime.of(ldt, usChicago);
		System.out.println(zdt);
// 2013-10-03T01:30 existed twice in America/Chicago time zone
		LocalDateTime ldt2 = LocalDateTime.of(2013, Month.NOVEMBER, 3, 1, 30);
		ZonedDateTime zdt2 = ZonedDateTime.of(ldt2, usChicago);
		System.out.println(zdt2);
// Try using the two rules for overlaps: one will use the earlier
// offset -05:00 (the default) and another the later offset -06:00
		System.out.println(zdt2.withEarlierOffsetAtOverlap());
		System.out.println(zdt2.withLaterOffsetAtOverlap());
	}

	@Test
	public void test_truncate() {
		System.out.println(OffsetTime.now());
		System.out.println(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
	}

	@Test
	public void test_instant() {
		//Instant封装了一个时间戳
		Instant.now().toEpochMilli();
	}

	@Test
	public void test_dateToLocalDate() {
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	@Test
	public void test_duration() {
		System.out.println(LocalDate.now().plus(Duration.ofDays(10)));
	}

	@Test
	public void test_between() {
		long d1 = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.now().plusDays(3));
		assertEquals(3, d1);
		long d2 = ChronoUnit.DAYS.between(LocalDate.now().plusDays(3), LocalDate.now());
		assertEquals(-3, d2);
	}

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
