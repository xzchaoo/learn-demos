package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import org.slf4j.Logger;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
public interface ContextLog {
  ContextLog addTag(String name, String value);

  void info(Logger logger, String message);

  void info(Logger logger, String message, Object arg);

  void info(Logger logger, String message, Object arg1, Object arg2);

  void info(Logger logger, String message, Object... args);
}
