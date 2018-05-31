package com.xzchaoo.learn.config.myconfig.biz;

import com.xzchaoo.learn.config.myconfig.configs.ConfigParseUtils;
import com.xzchaoo.learn.config.myconfig.core.Config;

import java.util.List;
import java.util.function.Function;

import javax.annotation.Resource;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public abstract class AbstractBizConfig implements RefreshableConfig {
  @Resource
  protected Config config;

  @Override
  public void refresh() {
    // empty
  }

  protected List<String> parseList(String key, char separator) {
    return parseList(key, separator, Function.identity());
  }

  protected <T> List<T> parseList(String key, char separator, Function<String, ? extends T> valueParser) {
    // return Collections.unmodifiableList(ConfigParseUtils.parseList(config.getString(key), separator, valueParser));
    return ConfigParseUtils.parseList(config.getString(key), separator, valueParser);
  }
}
