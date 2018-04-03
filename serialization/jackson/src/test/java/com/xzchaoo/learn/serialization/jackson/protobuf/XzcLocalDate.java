package com.xzchaoo.learn.serialization.jackson.protobuf;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class XzcLocalDate {
  private int year;
  private int month;
  private int day;
}
