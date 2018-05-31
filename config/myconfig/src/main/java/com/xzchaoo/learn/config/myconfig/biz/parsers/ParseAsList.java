package com.xzchaoo.learn.config.myconfig.biz.parsers;

/**
 * @author xzchaoo
 * @date 2018/6/1
 */
public @interface ParseAsList {
  Class<? extends Parser> valueParser() default Parser.None.class;

  char separator() default '|';
}
