package com.xzchaoo.learn.rxjava.custom.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposables;

/**
 * @author xzchaoo
 * @date 2018/5/9
 */
public class MySingleJust<T> extends Single<T> {
  private final T t;

  /**
   * 这里不会对T做非空检查, 因为这一般是外界需要做的事
   *
   * @param t
   */
  public MySingleJust(T t) {
    this.t = t;
  }

  @Override
  protected void subscribeActual(SingleObserver<? super T> observer) {
    observer.onSubscribe(Disposables.disposed());
    observer.onSuccess(t);
  }
}
