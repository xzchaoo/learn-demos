package com.xzchaoo.learn.rxjava.examples.spring5;

/**
 * @author zcxu
 * @date 2018/2/11 0011
 */
public class ServletServerExchange {
  private Object request;
  private Object response;

  public Object getRequest() {
    return request;
  }

  public void setRequest(Object request) {
    this.request = request;
  }

  public Object getResponse() {
    return response;
  }

  public void setResponse(Object response) {
    this.response = response;
  }
}
