package com.xzchaoo.learn.serialization.jackson.protobuf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xzchaoo.learn.serialization.jackson.protobuf.XzcLocalDate;

import java.time.LocalDate;
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

  @JsonProperty(index = 10)
//  @JsonSerialize(converter = XzcLocalDateToIntConverter.class)
//  @JsonDeserialize(converter = IntToXzcLocalDateConverter.class)
  private XzcLocalDate xzcLocalDate;

  @JsonProperty(index = 11)
  private LocalDate localDate;
  @JsonProperty(index = 12)
  private boolean aboolean;
  @JsonProperty(index = 13)
  private byte abyte;
  @JsonProperty(index = 14)
  private short ashort;

  @JsonProperty(index = 15)
  private boolean[] booleans;
  @JsonProperty(index = 16)
  private byte[] bytes;
  @JsonProperty(index = 17)
  private short[] shorts;
  @JsonProperty(index = 18)
  private int[] ints;
  @JsonProperty(index = 19)
  private long[] longs;
  @JsonProperty(index = 20)
  private float[] floats;
  @JsonProperty(index = 21)
  private double[] doubles;
}
