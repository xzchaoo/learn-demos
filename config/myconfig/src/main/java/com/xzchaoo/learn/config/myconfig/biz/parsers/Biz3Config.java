package com.xzchaoo.learn.config.myconfig.biz.parsers;

import com.xzchaoo.learn.config.myconfig.biz.Property;

import java.util.List;

import lombok.Getter;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
// @ConfigPrefix("foo")
public class Biz3Config {
  @Getter
  @Property("biz3.astring")
  private String astring;

  @Getter
  @Property("biz3.aint")
  private int aint;

  @Getter
  @Property("biz3.afloat")
  private float afloat;

  @Getter
  @Property("biz3.adouble")
  private double adouble;

  @Getter
  @Property("biz3.along")
  private long along;

  @Getter
  @Property("biz3.aboolean1")
  private boolean aboolean1;

  @Getter
  @Property("biz3.aboolean2")
  private boolean aboolean2;

  @Getter
  @Property(value = "biz3.aint2", defaultValue = "123")
  private int aint2;

  @Getter
  @Property(value = "biz3.stringlist")
  @ParseAsList
  private List<String> stringList;
}
