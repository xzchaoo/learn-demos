package com.xzchaoo.learn.rxjava.examples.topsortcache;

import java.util.Collection;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * @author zcxu
 * @date 2018/1/18
 */
public interface Cache {
  /**
   * 注意refresh返回的completable必须是一个cold的completable! 这意味着 所有内部包含的代码必须延迟到subscribe阶段执行
   *
   * @return
   */
  Completable refresh();

  Completable initRefresh();

  void clearInitRefreshMark();

  /**
   * 用于订阅更新消息
   *
   * @return
   */
  Flowable<Long> refreshFlowable();

  Collection<Cache> getDependencies();
}
