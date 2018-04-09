package com.xzchaoo.learn.lombok;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author zcxu
 * @date 2018/4/8 0008
 */
@Getter
@Setter
@ToString
public class FooEntity {
  private int id;
  private String username;
  private BarEntity bar;
}
