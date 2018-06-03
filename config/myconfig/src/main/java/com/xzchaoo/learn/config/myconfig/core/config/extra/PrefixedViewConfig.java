package com.xzchaoo.learn.config.myconfig.core.config.extra;


import com.xzchaoo.learn.config.myconfig.core.Config;
import com.xzchaoo.learn.config.myconfig.core.config.AbstractConfig;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xzchaoo
 * @date 2018/6/2
 */
public class PrefixedViewConfig extends AbstractConfig {
  private final Config parent;
  private final String prefix;
  private volatile State state;

  public PrefixedViewConfig(String name, Config parent, String prefix) {
    super(name);
    this.parent = parent;
    this.prefix = prefix.endsWith(".") ? prefix : (prefix + ".");
    this.state = new State(parent, prefix);
    parent.addListener(this::onParentChange);
  }

  private void onParentChange(Config parent) {
    this.state = new State(parent, prefix);
    super.notifyChanged();
  }

  @Override
  public String getString(String key) {
    return state.map.get(key);
  }

  @Override
  public String getString(String key, String defaultValue) {
    return state.map.getOrDefault(key, defaultValue);
  }

  @Override
  public boolean contains(String key) {
    return state.map.containsKey(key);
  }

  @Override
  public Map<String, String> asMap() {
    return state.map;
  }

  public Config getParent() {
    return parent;
  }

  private static class State {
    final Map<String, String> map;

    State(Config parent, String prefix) {
      Map<String, String> parentMap = parent.asMap();
      Map<String, String> map = new HashMap<>();
      for (Map.Entry<String, String> e : parentMap.entrySet()) {
        String key = e.getKey();
        if (key.startsWith(prefix)) {
          map.put(key, e.getValue());
        }
      }
      this.map = Collections.unmodifiableMap(map);
    }
  }

}
