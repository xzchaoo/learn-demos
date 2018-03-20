package com.xzchaoo.learn.test.mockito;

/**
 * @author zcxu
 * @date 2018/3/20 0020
 */
public class LogService {
  private LogConverter logConverter;

  public void send(String msg) {
    System.out.println(logConverter.convert(msg));
  }
}
