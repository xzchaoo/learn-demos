package com.xzchaoo.learn.rxjava.custom.maybe;

import java.util.Arrays;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.EmptyDisposable;

/**
 * @author xzcha
 * @date 2018/5/12
 */
public class MyMaybeZipIterable<T, U> extends Maybe<U> {
  private final Iterable<MaybeSource<? extends T>> sourceIterable;
  private final MaybeSource<? extends T>[] sources;
  private final Function<? super Object[], ? extends U> zipper;

  public MyMaybeZipIterable(Iterable<MaybeSource<? extends T>> sourceIterable, MaybeSource<? extends T>[] sources, Function<? super Object[], ? extends U> zipper) {
    this.sourceIterable = sourceIterable;
    this.sources = sources;
    this.zipper = zipper;
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void subscribeActual(MaybeObserver<? super U> observer) {
    MaybeSource<? extends T>[] sources;
    int size;

    MaybeSource<? extends T>[] temp = new MaybeSource[8];
    int index = 0;
    try {
      for (MaybeSource<? extends T> source : sourceIterable) {
        if (source == null) {
          EmptyDisposable.error(new NullPointerException("One of the sources is null"), observer);
          return;
        }
        if (index == temp.length) {
          temp = Arrays.copyOf(temp, index + (index >> 2));
        }
        temp[index++] = source;
      }
    } catch (Exception e) {
      Exceptions.throwIfFatal(e);
      EmptyDisposable.error(e, observer);
      return;
    }
    sources = temp;
    size = index;

    if (size == 0) {
      EmptyDisposable.complete(observer);
    } else if (size == 1) {
      sources[0].subscribe(new MyMaybeMap.MapSubscriber<T, U>(observer, new MyMaybeZipArray.AsSingleFunction<T, U>(zipper)));
    } else {
      // TODO 复用
      MyMaybeZipArray.ZipCoordinator<T, U> zc = new MyMaybeZipArray.ZipCoordinator<>(observer, size, zipper);
      observer.onSubscribe(zc);

      for (int i = 0; i < size; ++i) {
        if (zc.isDisposed()) {
          return;
        }

        MaybeSource<? extends T> source = sources[i];

        // 能否省略这里的NPE检查 对于iterable是不用检查额s
        if (source == null) {
          zc.onChildError(i, new NullPointerException("source is null"));
          return;
        }

        source.subscribe(zc.subscribes[i]);
      }
    }
  }
}
