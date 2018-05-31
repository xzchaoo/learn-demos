package com.xzchaoo.learn.config.myconfig.biz.parsers;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public interface ConfigProxy {
  <T> T getConfig(Class<T> clazz);
}
