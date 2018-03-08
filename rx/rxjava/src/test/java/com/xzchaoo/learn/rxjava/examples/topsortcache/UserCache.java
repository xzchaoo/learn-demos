package com.xzchaoo.learn.rxjava.examples.topsortcache;

import io.reactivex.Completable;

/**
 * @author zcxu
 * @date 2018/1/18
 */
public class UserCache extends AbstractCache implements IUserCache {
  private String value;

  @Override
  protected Completable doRefresh() {
    return Completable.create(se -> {
      //想想我是一个异步请求
      new Thread(() -> {
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        value = "user";
        System.out.println("user刷新完成");
        se.onComplete();
      }).start();
    });
  }

  @Override
  public Object getUser() {
    return value;
  }
}
