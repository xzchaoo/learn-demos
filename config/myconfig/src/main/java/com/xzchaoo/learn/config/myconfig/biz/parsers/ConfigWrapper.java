package com.xzchaoo.learn.config.myconfig.biz.parsers;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
@Getter
@AllArgsConstructor
public class ConfigWrapper {
  private final Object configInstance;
  private final ConfigConfig configConfig;
}
