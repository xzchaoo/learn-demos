package com.xzchaoo.learn.javassist;

/**
 * @author zcxu
 * @date 2018/3/8 0008
 */
public abstract class ServiceClientBase<T extends ServiceClientBase<T>> {
  protected static <T> T internalGetInstance(Class<T> childType) {
    try {
      return childType.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
