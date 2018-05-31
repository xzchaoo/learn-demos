package com.xzchaoo.learn.config.myconfig.biz;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public @interface ParseAsList {
  String key();

  char separator() default '|';

  Class<ValueParser<?>> parser();
}
