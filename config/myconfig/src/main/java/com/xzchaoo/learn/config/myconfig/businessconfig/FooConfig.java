package com.xzchaoo.learn.config.myconfig.businessconfig;

import com.xzchaoo.learn.config.myconfig.core.Config;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author zcxu
 * @date 2018/5/30
 */
public class FooConfig extends AbstractConfig {
  // 不加volatile会有一些潜在的问题 对大部分应用是无害的
  // private volatile List<String> bazList = Collections.emptyList();
  private List<String> bazList = Collections.emptyList();

  public FooConfig(Config config) {
    super(config);
  }

  public String getBar() {
    return config.getString("foo.bar", "BAR");
  }

  public List<String> getBazList() {
    return bazList;
  }

  @Override
  public void refresh() {
    List<String> newBazList = Arrays.asList(StringUtils.split(config.getString("foo.baz"), '/'));
    this.bazList = newBazList;
  }
}
