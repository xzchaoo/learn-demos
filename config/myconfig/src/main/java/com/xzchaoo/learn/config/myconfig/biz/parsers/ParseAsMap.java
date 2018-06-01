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
public @interface ParseAsMap {
  char separator() default '|';

  char separator2() default ':';

  Class<? extends Parser> keyParser() default Parser.None.class;

  Class<? extends Parser> valueParser() default Parser.None.class;
}
