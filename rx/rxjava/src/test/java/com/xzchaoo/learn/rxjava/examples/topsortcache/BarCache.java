package com.xzchaoo.learn.rxjava.examples.topsortcache;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zcxu
 * @date 2018/1/18
 */
public class BarCache extends AbstractCache implements IBarCache {
  private String value;

  @Override
  protected Completable doRefresh() {
    return Completable.fromAction(() -> {
      System.out.println("read something from DB");
      Thread.sleep(1000);
      value = "bar";
      System.out.println("bar刷新完成");
    }).subscribeOn(Schedulers.io());//run in IO Thread
  }

  @Override
  public Object getBar() {
    return value;
  }
}
