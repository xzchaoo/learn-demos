package com.xzchaoo.learn.config.myconfig.biz.parsers;

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
public @interface ParseAsList {
  char separator() default '|';
}
