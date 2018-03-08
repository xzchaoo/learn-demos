package com.xzchaoo.learn.rxjava.examples.spring5;

/**
 * @author zcxu
 * @date 2018/2/11 0011
 */
public class ServerExchange {
  private ServerRequest request;
  private ServerResponse response;

  public ServerRequest getRequest() {
    return request;
  }

  public void setRequest(ServerRequest request) {
    this.request = request;
  }

  public ServerResponse getResponse() {
    return response;
  }

  public void setResponse(ServerResponse response) {
    this.response = response;
  }
}
