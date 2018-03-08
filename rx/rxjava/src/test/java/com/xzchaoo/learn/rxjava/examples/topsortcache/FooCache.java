package com.xzchaoo.learn.rxjava.examples.topsortcache;

import javax.annotation.Nonnull;

import io.reactivex.Completable;

/**
 * @author zcxu
 * @date 2018/1/18
 */
public class FooCache extends AbstractCache implements IFooCache {

  private IBarCache barCache;
  private UserCache userCache;
  private Object foo = "empty";

  public FooCache(@Nonnull IBarCache barCache, @Nonnull UserCache userCache) {
    this.barCache = barCache;
    this.userCache = userCache;
  }

  @Override
  protected Completable doRefresh() {
    return Completable.fromAction(() -> {
      //由于foo的初始化很快 所以它就不异步了
      Object bar = barCache.getBar();
      Object user = userCache.getUser();
      foo = "" + bar + user;
      System.out.println("foo刷新完成");
    });
  }


  @Override
  public Object getFoo() {
    return foo;
  }


  protected Completable createInitRefreshCache() {
    //拓扑初始化可以保证 barCache userCache 的初始化不必FooCache晚
    return barCache.initRefresh().andThen(userCache.initRefresh()).andThen(this.refresh());
  }
}
