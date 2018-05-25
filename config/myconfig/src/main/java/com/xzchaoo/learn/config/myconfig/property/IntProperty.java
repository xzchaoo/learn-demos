package com.xzchaoo.learn.config.myconfig.property;

import com.xzchaoo.learn.config.myconfig.core.Subscription;

/**
 * @author xzchaoo
 * @date 2018/5/25
 */
public interface IntProperty {
  Integer get();

  int get(int defaultValue);

  Subscription subscribe(PropertyObserver<Integer> observer);
}
