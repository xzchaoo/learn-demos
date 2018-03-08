package com.xzchaoo.learn.rxjava.examples.scanvideos;

import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ScanVideoExample {
  private static Single<String> asyncHttpGet(int sleepMills, String fakeResponse) {
    return Single.create(se -> {
      Thread t = new Thread(() -> {
        try {
          Thread.sleep(sleepMills);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        se.onSuccess(fakeResponse);
      });
      se.setCancellable(t::interrupt);
      t.start();
    });
  }

  private static AtomicInteger concurrency = new AtomicInteger(0);

  private static Single<String> search(int aid) {
    int time = new Random().nextInt(500);
    long begin = System.currentTimeMillis();
    return asyncHttpGet(time, "视频" + aid + "的信息")
      .doOnSubscribe(ignore -> {
        System.out.println("doOnSubscribe 并发量=" + concurrency.incrementAndGet());
      })
      .doOnSuccess((ignore) -> {
        System.out.println("爬取了一个视频 耗时=" + (System.currentTimeMillis() - begin) + " doOnSuccess 并发量=" + concurrency.decrementAndGet());
      });
  }

  @Test
  public void test() {
    //假设现在要查询 [100,200) 的视频信息
    List<ScanVideoContext> result = Flowable.range(100, 100)
      .map(aid -> {
        ScanVideoContext ctx = new ScanVideoContext();
        ctx.aid = aid;
        return ctx;
      }).flatMapSingle(ctx -> search(ctx.aid).map(response -> {
        ctx.response = response;
        return ctx;
      }), true, 4)//最多4个并发
      .subscribeOn(Schedulers.io())
      .observeOn(Schedulers.computation())
      .toList()
      .blockingGet();
    System.out.println(result);
  }

  @Test
  public void test2() {
    //假设现在要查询 [100,200) 的视频信息
    List<ScanVideoContext> contexts = IntStream.range(100, 200).mapToObj(aid -> {
      ScanVideoContext ctx = new ScanVideoContext();
      ctx.aid = aid;
      return ctx;
    }).collect(Collectors.toList());

    Completable completable = Flowable.fromIterable(contexts)
      .flatMapCompletable(ctx -> search(ctx.aid).doOnSuccess(response -> {
        ctx.response = response;
      }).toCompletable(), true, 4);

    completable.blockingAwait();
    System.out.println("执行结束");
  }
}
