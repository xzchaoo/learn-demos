package com.xzchaoo.learn.config.myconfig.core.annotation;


import com.xzchaoo.learn.config.myconfig.core.parser.Parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于描述一个配置的属性
 *
 * @author xzchaoo
 * @date 2018/5/31
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
  /**
   * 是一个特殊的占位符, 表示没有默认值
   */
  String NONE = "#NONE#";

  /**
   * key 但是value可以省略不写
   *
   * @return key
   */
  String value();

  /**
   * 默认值
   *
   * @return 默认值
   */
  String defaultValue() default NONE;

  /**
   * 一级分隔符
   *
   * @return 一级分隔符
   */
  char separator() default '|';

  /**
   * 二级分隔符
   *
   * @return 二级分隔符
   */
  char separator2() default ':';

  /**
   * 自定义key的解析器, 仅对map类型有效, 用于解析Map的key
   *
   * @return 解析器的实现类
   */
  Class<? extends Parser> keyParser() default Parser.None.class;

  /**
   * 自定义value的解析器, 用于解析 普通类型 或 List 或 Map的value
   *
   * @return 解析器的实现类
   */
  Class<? extends Parser> valueParser() default Parser.None.class;
}
