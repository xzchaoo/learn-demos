package com.xzchaoo.learn.config.myconfig.core.parser;

/**
 * @author xzchaoo
 * @date 2018/6/1
 */
public interface ParserProvider {
  /**
   * 获得对应类的parser
   *
   * @param clazz
   * @param <T>
   * @return
   */
  <T> Parser<T> getParser(Class<T> clazz);

  /**
   * 获得对应类的parser
   *
   * @param clazz
   * @param defaultParserClass
   * @param <T>
   * @return
   */
  <T> Parser<T> getParser(Class<T> clazz, Class<? extends Parser<? extends T>> defaultParserClass);
}
