package com.xzchaoo.learn.rxjava.examples.scanvideo2;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * @author zcxu
 * @date 2018/2/1
 */
public class ScanVideo2Test {
  @Test
  public void test() {
    List<Integer> ids = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());

    Flowable.fromIterable(ids)
      .flatMapMaybe(this::searchVideo, true, 10)
      .buffer(100)
      .blockingForEach(this::saveToDB);
    System.out.println("主线程结束");
  }

  private void saveToDB(List<VideoEntity> list) {
    System.out.println("保存到DB " + list.size());
  }

  private Single<String> httpGetSingle(String url) {
    return Single.create(emitter -> {
      ListenableFuture<String> lf = asyncHttpGet("url");
      emitter.setCancellable(() -> lf.cancel(true));

      Futures.addCallback(lf, new FutureCallback<String>() {
        @Override
        public void onSuccess(@Nullable String result) {
          emitter.onSuccess(result);
        }

        @Override
        public void onFailure(Throwable t) {
          emitter.onError(t);
        }
      }, MoreExecutors.directExecutor());
    });
  }

  private Maybe<VideoEntity> searchVideo(int id) {
    return httpGetSingle("video.url/" + id)
      .retry(1)
      .timeout(10, TimeUnit.SECONDS)
      .filter(title -> title.startsWith("foo"))
      .map(title -> {
        VideoEntity ve = new VideoEntity();
        ve.setId(id);
        ve.setTitle(title);
        return ve;
      });
  }

  private ListenableFuture<String> asyncHttpGet(String url) {
    SettableFuture<String> sf = SettableFuture.create();
    new Thread(() -> {
      Random r = new Random();
      try {
        Thread.sleep(r.nextInt(500) + 1);
        //Thread.sleep(1);
      } catch (Exception e) {
      }
      sf.set("foo title");
    }).start();
    return sf;
  }
}
