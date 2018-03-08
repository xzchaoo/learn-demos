package com.xzchaoo.learn.rxjava;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;

import org.junit.Test;
import org.reactivestreams.Publisher;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author zcxu
 * @date 2018/2/1
 */
public class FutureTest {
  @Test
  public void test_timer() {
    Single.just(1).delay(1, TimeUnit.SECONDS);
  }

  @Test
  public void test_error_handling() throws InterruptedException {
    Single.fromCallable(() -> {
      Thread.sleep(100);
      return "123";
    }).subscribeOn(Schedulers.io())
      .map(Integer::parseInt)
      .subscribe(result -> {
        System.out.println("结果是 " + result);
      });
    System.out.println("结束");
    Thread.sleep(1000);
  }

  @Test
  public void test_timeout() {
    Single<Object> yourS = null;
    yourS = yourS.timeout(1, SECONDS);
  }

  @Test
  public void test_retry() throws InterruptedException {
    Single<Object> yourS = Single.error(new IllegalArgumentException()).delay(100, TimeUnit.MILLISECONDS, true);
    yourS.retryWhen(new Function<Flowable<Throwable>, Publisher<?>>() {
      @Override
      public Publisher<?> apply(Flowable<Throwable> errorF) throws Exception {
        long begin = System.currentTimeMillis();
        final boolean[] first = new boolean[]{true};
        return errorF.flatMapSingle(error -> {
          if (first[0] && System.currentTimeMillis() - begin <= 1000 && (error instanceof IllegalArgumentException)) {
            System.out.println("进行一次重试");
            first[0] = false;
            return Single.just("");
          }
          first[0] = false;
          return Single.error(error);
        });
      }
    }).subscribe(result -> {
      System.out.println("我是结果");
    }, error -> {
      System.out.println("收到一个错误" + error);
    });
    Thread.sleep(3000);
  }

  @Test
  public void test_zip() {
    Flowable<Character> f1 = Flowable.just('a');
    Flowable<Integer> f2 = Flowable.just(1);
    Flowable<String> f3 = Flowable.zip(f1, f2, (a, b) -> a.toString() + "-" + b.toString());


    int x = 800;
    Flowable<Long> timerF = Flowable.timer(x, TimeUnit.MILLISECONDS);
    Flowable<Object> yourF = null;
    Flowable<Object> mergedF = Flowable.zip(timerF, yourF, (a, b) -> b);
  }

  @Test
  public void test_map() {
    Flowable<String> s = null;
    Flowable<Integer> s2 = s.map(a -> {
      return Integer.parseInt(a.substring(1));
    });


    Flowable.create(new FlowableOnSubscribe<Integer>() {
      @Override
      public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
        Thread t = new Thread(() -> {
          try {
            emitter.onNext(1);
            Thread.sleep(100);
            emitter.onNext(2);
            Thread.sleep(100);
            emitter.onNext(3);
            Thread.sleep(100);
            emitter.onComplete();
          } catch (InterruptedException e) {
            emitter.onError(e);
          }
        });
        t.start();
      }
    }, BackpressureStrategy.BUFFER);
  }

  @Test
  public void test() {
    ListenableFuture<String> lf = callAsync();
    lf.addListener(new Runnable() {
      @Override
      public void run() {
        try {
          String s = lf.get();
        } catch (Exception e) {
        }
      }
    }, MoreExecutors.directExecutor());
    lf.cancel(true);
  }

  private ListenableFuture<String> callAsync() {
    return null;
  }
}
