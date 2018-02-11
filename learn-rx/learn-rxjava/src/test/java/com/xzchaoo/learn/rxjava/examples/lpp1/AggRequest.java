package com.xzchaoo.learn.rxjava.examples.lpp1;

/**
 * @author zcxu
 * @date 2018/2/11 0011
 */
public class AggRequest {
  private String fakeRequestJson;

  public AggRequest() {
  }

  public AggRequest(String fakeRequestJson) {
    this.fakeRequestJson = fakeRequestJson;
  }

  public String getFakeRequestJson() {
    return fakeRequestJson;
  }

  public void setFakeRequestJson(String fakeRequestJson) {
    this.fakeRequestJson = fakeRequestJson;
  }
}
