package com.xzchaoo.learn.config.myconfig.biz.parsers;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
@Getter
@AllArgsConstructor
public class PropertyConfig<T> {
  private final String key;
  private final Class<T> clazz;
  private final Parser<T> parser;
  private final String defaultValue;
}
