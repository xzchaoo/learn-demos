package com.xzchaoo.learn.serialization.gson;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zcxu
 * @date 2018/2/28 0028
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
  private int id;

  //属性重命名
  @SerializedName("un")
  private String username;

  private Date date1;
  private LocalDate date2;
}
