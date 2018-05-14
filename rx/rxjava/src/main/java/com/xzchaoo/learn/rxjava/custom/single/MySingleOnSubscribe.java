package com.xzchaoo.learn.rxjava.custom.single;

import io.reactivex.annotations.NonNull;

/**
 * @author xzcha
 * @date 2018/5/10
 */
@FunctionalInterface
public interface MySingleOnSubscribe<T> {
  /**
   * Called for each SingleObserver that subscribes.
   *
   * @param emitter the safe emitter instance, never null
   * @throws Exception on error
   */
  void subscribe(@NonNull MySingleEmitter<T> emitter) throws Exception;
}
