package com.xzchaoo.learn.config.myconfig.biz.parsers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xzchaoo
 * @date 2018/6/1
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface ParseAsList {
  Class<? extends Parser> valueParser() default Parser.None.class;

  char separator() default '|';
}
