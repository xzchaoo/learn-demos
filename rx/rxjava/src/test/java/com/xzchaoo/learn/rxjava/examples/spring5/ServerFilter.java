package com.xzchaoo.learn.rxjava.examples.spring5;

import io.reactivex.Single;

/**
 * @author zcxu
 * @date 2018/2/11 0011
 */
public interface ServerFilter {
  Single<ServerExchange> filter(ServerExchange exchange);
}
