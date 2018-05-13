package com.xzchaoo.learn.rxjava.custom;

import com.xzchaoo.learn.rxjava.custom.maybe.MyMaybeTimeoutMaybe;

import org.junit.Test;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

/**
 * @author xzcha
 * @date 2018/5/13
 */
public class MyMaybeTimeoutMaybeTest {
  @Test
  public void test() throws InterruptedException {
    Maybe<Integer> source = Maybe.just(1);
    Maybe<Integer> timeout = Maybe.empty();
    Maybe<Integer> fallback = Maybe.just(0);
    new MyMaybeTimeoutMaybe<>(source, timeout, fallback).subscribe(new MaybeObserver<Integer>() {
      Disposable d;

      @Override
      public void onSubscribe(Disposable d) {
        this.d = d;
        d.dispose();
        System.out.println(d.isDisposed());
      }

      @Override
      public void onSuccess(Integer integer) {
        // d.dispose();
        System.out.println(d.isDisposed());
        System.out.println("success " + integer);
      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
      }

      @Override
      public void onComplete() {
        System.out.println("complete");
      }
    });
    Thread.sleep(3000);
  }
}
