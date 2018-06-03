package com.xzchaoo.learn.config.myconfig.demo;


import com.xzchaoo.learn.config.myconfig.core.annotation.Property;

import java.awt.Point;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

/**
 * TODO key名自动推断
 *
 * @author xzchaoo
 * @date 2018/5/31
 */
@Getter
public class Biz3Config {
  // 解析上也支持基本属性类型的解析
  @Property("biz3.astring")
  private String astring;

  @Property("biz3.aint")
  private int aint;

  @Property("biz3.afloat")
  private float afloat;

  @Property("biz3.adouble")
  private double adouble;

  @Property("biz3.along")
  private long along;

  @Property("biz3.aboolean1")
  private boolean aboolean1;

  @Property("biz3.aboolean2")
  private boolean aboolean2;

  @Property(value = "biz3.aint2", defaultValue = "123")
  private int aint2;

  @Property(value = "biz3.stringlist")
  private List<String> stringList;

  @Property(value = "biz3.map1")
  private Map<String, Integer> map1;

  @Property(value = "biz3.set1", defaultValue = "1,2", valueParser = PointParser.class)
  private Set<Point> set1;
}
