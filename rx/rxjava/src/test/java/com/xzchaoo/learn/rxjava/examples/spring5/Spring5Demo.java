package com.xzchaoo.learn.rxjava.examples.spring5;

import org.junit.Test;

import io.reactivex.Single;
import io.reactivex.SingleSource;

/**
 * @author zcxu
 * @date 2018/2/11 0011
 */
public class Spring5Demo {
  @Test
  public void test() {
    ServletServerExchange servlet = new ServletServerExchange();
    Single.just(servlet)
      .map(this::convertToModel)
      .compose(this::processInternalFilters)
      .compose(this::processServerFilters)
      .flatMap(this::callController)
      .subscribe(result -> {
        System.out.println("结果是 " + result);
      }, error -> {
        error.printStackTrace();
      });
  }

  private SingleSource<ServerExchange> callController(ServerExchange exchange) {
    //call your controller
    Object obj = callUserController(exchange);
    if (obj instanceof Single) {
      return (Single<ServerExchange>) obj;
    } else {
      return Single.just(exchange);
    }
  }


  //调用用户的controller接口
  private Object callUserController(ServerExchange exchange) {
    return "";
  }

  private SingleSource<ServerExchange> processServerFilters(Single<ServerExchange> exchange) {
    ServerFilter sf1 = Single::just;
    ServerFilter sf2 = Single::just;
    return exchange.flatMap(sf1::filter)
      .flatMap(sf2::filter);
  }

  private Single<ServerExchange> processInternalFilters(Single<ServerExchange> exchange) {
    ServerFilter sf1 = Single::just;
    ServerFilter sf2 = Single::just;
    return exchange.flatMap(sf1::filter)
      .flatMap(sf2::filter);
  }

  private ServerExchange convertToModel(ServletServerExchange servlet) {
    return new ServerExchange();
  }
}
