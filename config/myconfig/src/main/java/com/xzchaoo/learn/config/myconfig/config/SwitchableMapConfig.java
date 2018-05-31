package com.xzchaoo.learn.config.myconfig.config;

import java.util.Collections;
import java.util.Map;

/**
 * @author xzchaoo
 * @date 2018/5/25
 */
public class SwitchableMapConfig extends MapConfig {
  private static final Map<String, String> EMPTY = Collections.emptyMap();
  private Map<String, String> backup = EMPTY;

  private volatile boolean on;

  public SwitchableMapConfig(Map<String, String> map) {
    this(map, true);
  }

  public SwitchableMapConfig(Map<String, String> map, boolean on) {
    super(on ? map : EMPTY);
    if (map == null) {
      throw new NullPointerException();
    }
    this.on = on;
    if (!on) {
      backup = map;
    }
  }

  @Override
  public synchronized void replace(Map<String, String> map) {
    if (on) {
      super.replace(map);
    } else {
      this.backup = map;
    }
  }

  public synchronized void setState(boolean on) {
    if (this.on != on) {
      this.on = on;
      if (on) {
        super.replace(backup);
      } else {
        backup = super.map;
        super.replace(EMPTY);
      }
    }
  }

  public boolean getState() {
    return on;
  }
}
