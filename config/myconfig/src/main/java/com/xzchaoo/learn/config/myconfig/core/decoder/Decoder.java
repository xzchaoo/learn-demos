package com.xzchaoo.learn.config.myconfig.core.decoder;

/**
 * @author xzchaoo
 * @date 2018/6/2
 */
public interface Decoder {
  /**
   * 将str解码为T的实例
   *
   * @param clazz
   * @param str
   * @param <T>
   * @return
   */
  <T> T decode(Class<T> clazz, String str);
}
