package com.xzchaoo.learn.rxjava.examples.lpp1;

/**
 * @author zcxu
 * @date 2018/2/11 0011
 */
public class AggContext {
  private final AggRequest ar;
  private AggSearchResult result;
  private int errorCode;

  public AggContext(AggRequest ar) {
    this.ar = ar;
  }

  public AggRequest getAr() {
    return ar;
  }

  public void setResult(AggSearchResult result) {
    this.result = result;
  }

  public AggSearchResult getResult() {
    return result;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return errorCode;
  }
}
