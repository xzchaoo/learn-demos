package com.xzchaoo.learn.config.myconfig.core.config.extra;

/**
 * 总是为空的Config
 *
 * @author xzchaoo
 * @date 2018/6/2
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class EmptyConfig extends AbstractUnmodifiableMapConfig {
  public static final EmptyConfig INSTANCE = new EmptyConfig();

  public EmptyConfig() {
    super("Empty", null);
  }
}
