package com.xzchaoo.learn.test.demo20180412.utils;

import java.util.Collection;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public class CollectionUtils {
  private CollectionUtils() {
  }

  public static boolean isEmpty(Collection<?> coll) {
    return coll == null || coll.isEmpty();
  }
}
