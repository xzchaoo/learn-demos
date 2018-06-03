package com.xzchaoo.learn.config.myconfig.core.parser;

import java.util.function.Function;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public interface Parser<T> extends Function<String, T> {
  T parse(String str);

  @Override
  default T apply(String str) {
    return parse(str);
  }

  final class None implements Parser<Object> {
    @Override
    public Object parse(String str) {
      throw new UnsupportedOperationException();
    }
  }
}
