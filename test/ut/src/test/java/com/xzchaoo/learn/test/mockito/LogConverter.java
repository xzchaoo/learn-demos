package com.xzchaoo.learn.test.mockito;

/**
 * @author zcxu
 * @date 2018/3/20 0020
 */
public class LogConverter {
  private ObjectMapper objectMapper;

  public String convert(String msg) {
    return "C " + objectMapper.convert(msg);
  }
}
