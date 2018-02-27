package com.xzchaoo.learn.jdk8.time;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2017/3/21.
 */
public class TimeTest {

  @Test
  public void test_localdatetime_interval() {
    //检查2个 LDT 的时间差
    //用 天 分 秒 来表示
  }

  @Test
  public void test_period() {
    //Period返回一种日期间隔的陈述方式, 比如 即1个月又2天, 它具体表示多少天是不一定的 因为每个月的天数不一样
    //比如2月2日与1月1日的period是 1个月又1天
    Period p = Period.between(LocalDate.now(), LocalDate.now().minusYears(1).plusDays(2));
    System.out.println(p.getDays());
    System.out.println(p);
  }

  @Test
  public void test_mills() {
    //构造函数不支持毫秒 但可以通过其它方式做到
    System.out.println(LocalDateTime.of(1, 1, 1, 1, 1, 1).plus(1, ChronoUnit.MILLIS));
  }

  @Test
  public void test_truncateTo() {
    LocalDateTime time = LocalDateTime.of(2017, 1, 1, 1, 1, 1, 1);
    //将时间截到 分 单位
    LocalDateTime timeTruncatedToMinute = time.truncatedTo(ChronoUnit.MINUTES);
    assertEquals(1, timeTruncatedToMinute.getHour());
    assertEquals(1, timeTruncatedToMinute.getMinute());
    assertEquals(0, timeTruncatedToMinute.getSecond());
    assertEquals(0, timeTruncatedToMinute.getNano());
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
  public void test_between() {
    //计算两个时间的间隔 注意这边总是会用后面的日期去减前面的日期 结果可能是一个负数哦
    long d1 = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.now().plusDays(3));
    assertEquals(3, d1);
    long d2 = ChronoUnit.DAYS.between(LocalDate.now().plusDays(3), LocalDate.now());
    assertEquals(-3, d2);
  }

  @Test
  public void test_parse() {
    // yyyy-MM-dd 只能支持 2017-01-02 不能支持 2017-1-2
    //而 yyyy-M-d 则可以支持上述两种
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-d");
    System.out.println(dtf.parse("2017-11-23", LocalDate::from));
  }

  @Test
  public void test_LocalTime() {
    LocalTime parse = LocalTime.parse("00:06:00");
    assertEquals(0, parse.getHour());
    assertEquals(6, parse.getMinute());
    assertEquals(0, parse.getSecond());

    //省略秒也是可以的
    parse = LocalTime.parse("00:06");
    assertEquals(0, parse.getHour());
    assertEquals(6, parse.getMinute());
    assertEquals(0, parse.getSecond());
  }

  @Test
  public void test_mills_to_LocalDateTime() {
    long mills = 1;
    LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochMilli(mills), ZoneId.of("Asia/Shanghai"));
    System.out.println(ldt);
    //注意这里接的是纳秒
    assertEquals(LocalDateTime.of(1970, 1, 1, 8, 0, 0, 1000000), ldt);
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

