package com.xzchaoo.learn.rxjava.examples.lpp1;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * @author zcxu
 * @date 2018/2/11 0011
 */
public class Lpp1App {

  @Test
  public void test() {
    //假设这里有很多城市对
    List<TripCityPair> cityPairList = new ArrayList<TripCityPair>(100);
    //对于每个城市对
    Flowable.fromIterable(cityPairList)
      //根据他是 OW 还是 RT, 生成OW90 RT45:1-7 的请求
      .flatMap(cp -> Flowable.<AggRequest>create(emitter -> {
        try {
          emitRequest(cp, emitter);
          emitter.onComplete();
        } catch (Exception e) {
          emitter.onError(e);
        }
      }, BackpressureStrategy.BUFFER))
      //对于上面生成的请求 最多10个并发执行
      .flatMapCompletable(ar -> search(ar).toCompletable(), false, 10)
      //阻塞直到全部查询结束
      .blockingAwait();
  }

  private Single<AggContext> search(AggRequest ar) {
    AggContext ctx = new AggContext(ar);
    int timeoutSeconds = 1;
    //先找缓存
    return findCache(ctx)
      //找不到再去call服务 如果callAggService的实现本身已经具备delay到subscribe的时候才执行 那么这里就没必要调用defer方法
      //这里将10秒放到callAggService上
      .switchIfEmpty(Single.defer(() -> callAggService(ctx)).timeout(timeoutSeconds, TimeUnit.SECONDS))
      //10秒不返回就算超时 (缓存+call) 算在一起的
      //.timeout(timeoutSeconds, TimeUnit.SECONDS)
      .map(result -> {
        ctx.setResult(result);
        return ctx;
      })
      .onErrorResumeNext(error -> {
        //假设超时的 errorCode 是1
        if (error instanceof TimeoutException) {
          ctx.setErrorCode(1);
        } else {
          ctx.setErrorCode(-1);
        }
        return Single.just(ctx);
      });
  }

  /**
   * 注意这里
   *
   * @param ctx
   * @return
   */
  private Maybe<AggSearchResult> findCache(AggContext ctx) {
    return Maybe.empty();
  }

  private Single<AggSearchResult> callAggService(AggContext ctx) {
    return Single.just(new AggSearchResult());
  }

  private void emitRequest(TripCityPair cp, FlowableEmitter<AggRequest> emitter) {
    LocalDate today = LocalDate.now();
    //OW90
    if ("OW".equals(cp.getTripType())) {
      String prefix = "OW:" + cp.getDepartureCityCode() + "-" + cp.getArrivalCityCode() + ":";
      for (int i = 0; i < 90; ++i) {
        String fakeRequestJson = prefix + today.plusDays(i);
        emitter.onNext(new AggRequest(fakeRequestJson));
      }
    } else {
      //RT45:1-7
      String prefix = "RT:" + cp.getDepartureCityCode() + "-" + cp.getArrivalCityCode() + ":";
      for (int i = 0; i < 45; ++i) {
        LocalDate out = today.plusDays(i);
        String prefix2 = prefix + "" + out + "-";
        for (int j = 1; j <= 7; ++j) {
          LocalDate in = out.plusDays(j);
          String fakeRequestJson = prefix2 + in;
          emitter.onNext(new AggRequest(fakeRequestJson));
        }
      }
    }
  }
}
