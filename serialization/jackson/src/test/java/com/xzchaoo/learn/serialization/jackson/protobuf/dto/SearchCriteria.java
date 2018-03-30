package com.xzchaoo.learn.serialization.jackson.protobuf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Calendar;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * index不能混用 如果一个指定了 那么其他的必须都指定
 */
@Getter
@Setter
@ToString
public class SearchCriteria {
  @JsonProperty(index = 1)
  private int aint;
  @JsonProperty(index = 2)
  private long along;
  @JsonProperty(index = 3)
  private float afloat;
  @JsonProperty(index = 4)
  private double adouble;
  @JsonProperty(index = 5)
  private String astring;
  @JsonProperty(index = 6)
  private Date adate;
  @JsonProperty(index = 7)
  private Calendar acalendar;
  @JsonProperty(index = 8)
  private int[] intArray;
  @JsonProperty(index = 9)
  private long[] longArray;
}
