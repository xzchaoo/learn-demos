package com.xzchaoo.learn.config.myconfig.core.config.extra;

import java.util.Map;

/**
 * TODO 系统属性是会变的, 但我们没法监听变化, 所以就认为它是不变的
 *
 * @author xzchaoo
 * @date 2018/5/25
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class SystemPropertyConfig extends AbstractUnmodifiableMapConfig {
  public static final SystemPropertyConfig INSTANCE = new SystemPropertyConfig();

  public SystemPropertyConfig() {
    super("System", getSystemPropertiesAsMap());
  }

  private static Map<String, String> getSystemPropertiesAsMap() {
    return convertToMap(System.getProperties());
  }

}
