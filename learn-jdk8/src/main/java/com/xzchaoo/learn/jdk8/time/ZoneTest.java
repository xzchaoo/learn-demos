package com.xzchaoo.learn.jdk8.time;

import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * created by zcxu at 2017/10/30
 *
 * @author zcxu
 */
public class ZoneTest {
    @Test
    public void test() {
        System.out.println(ZoneId.getAvailableZoneIds());
        System.out.println(ZoneOffset.UTC);
        System.out.println(LocalDate.now().atStartOfDay(ZoneOffset.UTC));
    }
}
