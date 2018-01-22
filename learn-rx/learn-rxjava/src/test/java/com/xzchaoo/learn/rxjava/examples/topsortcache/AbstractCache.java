package com.xzchaoo.learn.rxjava.examples.topsortcache;

import java.util.Collection;

import javax.annotation.Nonnull;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * @author zcxu
 * @date 2018/1/18
 */
public abstract class AbstractCache implements Cache {
  private Completable initRefreshCache;
  private final FlowableProcessor<Long> refreshPublishProcessor;
  private final Flowable<Long> refreshFlowable;

  public AbstractCache() {
    //TODO lazy
    refreshPublishProcessor = PublishProcessor.<Long>create().toSerialized();
    refreshFlowable = refreshPublishProcessor.hide();
  }

  //private AtomicReference<Completable> refreshing = new AtomicReference<>();

  @Override
  public final Completable refresh() {
    return doRefresh().doFinally(() -> {
      refreshPublishProcessor.onNext(0L);
    });

//    Completable refreshingC = refreshing.get();
//    //TODO 防止并发更新
//    if (refreshingC == null) {
//      synchronized (this) {
//        refreshingC = refreshing.get();
//        if (refreshingC == null) {
//          refreshingC = doRefresh();
//          Completable refreshingC2 = refreshingC;
//          refreshingC.doFinally(() -> {
//            refreshing.compareAndSet(refreshingC2, null);
//          }).doOnComplete(() -> {
//            refreshPublishProcessor.onNext(0L);
//          }).cache();
//        }
//      }
//    }
//    return refreshingC;
  }

  protected abstract Completable doRefresh();

  @Override
  public Completable initRefresh() {
    Completable ret = initRefreshCache;
    if (initRefreshCache == null) {
      synchronized (this) {
        if (initRefreshCache == null) {
          ret = initRefreshCache = createInitRefreshCache().cache();
        } else {
          ret = initRefreshCache;
        }
      }
    }
    return ret;
  }

  @Nonnull
  protected Completable createInitRefreshCache() {
    return refresh();
  }

  @Override
  public Flowable<Long> refreshFlowable() {
    return refreshFlowable;
  }

  @Override
  public void clearInitRefreshMark() {
    initRefreshCache = null;
  }

  @Override
  public Collection<Cache> getDependencies() {
    return null;
  }
}
