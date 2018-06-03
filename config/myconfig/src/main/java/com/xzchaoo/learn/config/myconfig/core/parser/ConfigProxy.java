package com.xzchaoo.learn.config.myconfig.core.parser;

/**
 * 用于将一个Config和一个pojo的属性绑定起来
 *
 * @author xzchaoo
 * @date 2018/5/31
 */
public interface ConfigProxy {
  /**
   * 将一个实例和配置绑定起来
   *
   * @param clazz clazz
   * @param <T>   泛型
   * @return T的实例
   */
  <T> T getConfig(Class<T> clazz);
}
