package com.xzchaoo.learn.javassist;

/**
 * @author zcxu
 * @date 2018/3/8 0008
 */
public abstract class ServiceClientBase<T extends ServiceClientBase<?>> {
  public static <T extends ServiceClientBase<T>> T getInstance() {
    return (T) null;
  }
}
