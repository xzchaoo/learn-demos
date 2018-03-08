package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import java.time.LocalDate;

import lombok.Data;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
@Data
public class DateRange {
  //included
  private LocalDate begin;
  //included
  private LocalDate end;
}
