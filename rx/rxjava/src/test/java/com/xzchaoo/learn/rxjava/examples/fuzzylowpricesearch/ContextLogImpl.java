package com.xzchaoo.learn.rxjava.examples.fuzzylowpricesearch;

import org.slf4j.Logger;

/**
 * @author zcxu
 * @date 2018/3/1 0001
 */
public class ContextLogImpl implements ContextLog {
  private TagHolder tagHolder = new TagHolder();

  @Override
  public ContextLog addTag(String name, String value) {
    tagHolder.add(name, value);
    return this;
  }

  @Override
  public void info(Logger logger, String message) {
    logger.info(wrap(message));
  }

  @Override
  public void info(Logger logger, String message, Object arg) {
    logger.info(wrap(message), arg);
  }

  @Override
  public void info(Logger logger, String message, Object arg1, Object arg2) {
    logger.info(wrap(message), arg1, arg2);
  }

  @Override
  public void info(Logger logger, String message, Object... args) {
    logger.info(wrap(message), args);
  }

  private String wrap(String message) {
    return tagHolder.toString() + message;
  }
}
