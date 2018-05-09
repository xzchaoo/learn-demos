package com.xzchaoo.learn.rxjava.custom.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;

/**
 * @author xzchaoo
 * @date 2018/5/9
 */
public class MySingleJust<T> extends Single<T> {
  @Override
  protected void subscribeActual(SingleObserver<? super T> observer) {

  }
}
