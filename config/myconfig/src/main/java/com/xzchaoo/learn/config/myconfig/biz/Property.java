package com.xzchaoo.learn.config.myconfig.biz;

import com.xzchaoo.learn.config.myconfig.biz.parsers.Parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
  String NONE = "#NONE#";

  /**
   * key 但是value可以省略不写
   *
   * @return
   */
  String value();

  String defaultValue() default NONE;

  Class<? extends Parser> keyParser() default Parser.None.class;

  Class<? extends Parser> valueParser() default Parser.None.class;

  char separator() default '|';

  char separator2() default ':';

  boolean asList() default false;

  boolean asMap() default false;
}
