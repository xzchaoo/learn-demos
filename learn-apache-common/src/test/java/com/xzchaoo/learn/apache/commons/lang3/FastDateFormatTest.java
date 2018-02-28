package com.xzchaoo.learn.apache.commons.lang3;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author zcxu
 * @date 2018/2/28 0028
 */
public class FastDateFormatTest {
  @Test
  public void test() throws ParseException {
    FastDateFormat fdf = FastDateFormat.getInstance("yyyyMMdd");
    Date date = DateUtils.parseDate("20170101", "yyyyMMdd");
    assertThat(fdf.format(date)).isEqualTo("20170101");
  }
}
