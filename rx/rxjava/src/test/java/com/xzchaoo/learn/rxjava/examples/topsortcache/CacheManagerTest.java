package com.xzchaoo.learn.rxjava.examples.topsortcache;

import com.google.common.collect.Lists;

import org.junit.Test;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author zcxu
 * @date 2018/1/18
 */
public class CacheManagerTest {
  @Test
  public void refreshAll() {
    for (int i = 0; i < 10; ++i) {
      BarCache bar = new BarCache();
      UserCache user = new UserCache();
      FooCache foo = new FooCache(bar, user);
      List<Cache> caches = Lists.newArrayList(foo, bar, user);
      long begin = System.currentTimeMillis();
      Flowable.fromIterable(caches)
        .flatMapCompletable(Cache::initRefresh, true, 10)
        .blockingAwait();
      long end = System.currentTimeMillis();
      System.out.println("耗时=" + (end - begin));
      System.out.println(foo.getFoo());
      System.out.println();
    }
  }
}
