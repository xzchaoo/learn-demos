package com.xzchaoo.learn.lombok;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author zcxu
 * @date 2018/4/8 0008
 */
@Getter
@Builder
@ToString
public class BarEntity {
  private int aint;
  private long along;
}
