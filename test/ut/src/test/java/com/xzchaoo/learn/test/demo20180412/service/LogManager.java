package com.xzchaoo.learn.test.demo20180412.service;

import com.xzchaoo.learn.test.demo20180412.utils.LogUtils;

/**
 * @author xzchaoo
 * @date 2018/4/12
 */
public class LogManager {
  public String log() {
    return LogUtils.add("log", DbManager.hello2());
  }
}
