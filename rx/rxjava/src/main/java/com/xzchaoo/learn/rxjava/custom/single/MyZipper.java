package com.xzchaoo.learn.rxjava.custom.single;

/**
 * @author xzchaoo
 * @date 2018/5/9
 */
@FunctionalInterface
public interface MyZipper<R> {
  R zip(Object[] args);
}
