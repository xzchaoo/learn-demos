package com.xzchaoo.learn.config.myconfig.biz.parsers;

import java.util.function.Function;

/**
 * @author xzchaoo
 * @date 2018/6/1
 */
public class ParserFunction<T> implements Function<String, T> {
  private final Class<T> clazz;
  private final Parser<T> parser;

  public ParserFunction(Class<T> clazz, Parser<T> parser) {
    this.clazz = clazz;
    this.parser = parser;
  }

  @Override
  public T apply(String s) {
    return parser.parse(clazz, s);
  }
}
