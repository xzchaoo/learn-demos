package com.xzchaoo.learn.config.myconfig.configs;

import com.xzchaoo.learn.config.myconfig.core.Config;
import com.xzchaoo.learn.config.myconfig.core.Subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xzchaoo
 * @date 2018/5/25
 */
public class CompositeConfig extends AbstractConfig {
  private static final CompositeState EMPTY = new CompositeState(Collections.emptyList(), Collections.emptyMap(), Collections.emptyList());
  private CompositeState state = EMPTY;

  public CompositeConfig() {
  }

  public CompositeConfig(List<Config> configs) {
    if (configs == null) {
      throw new NullPointerException();
    }

    synchronized (this) {
      this.state = buildNewState(configs);
    }
  }

  public void replace(List<Config> configs) {
    if (configs == null) {
      throw new NullPointerException();
    }
    // cancel old
    CompositeState oldState = this.state;
    oldState.holders.forEach(Subscription::cancel);

    synchronized (this) {
      this.state = buildNewState(configs);
    }
    triggerChange();
  }

  private void onChildChange() {
    synchronized (this) {
      CompositeState oldState = this.state;
      this.state = buildNewState(oldState);
    }
    triggerChange();
  }

  @Override
  public String getString(String key) {
    return this.state.map.get(key);
  }

  @Override
  public String getString(String key, String defaultValue) {
    return this.state.map.getOrDefault(key, defaultValue);
  }

  @Override
  public Map<String, String> getAsMap() {
    return this.state.map;
  }

  private CompositeState buildNewState(CompositeState oldState) {
    Map<String, String> map = new HashMap<>(oldState.map.size());
    for (Config config : oldState.configs) {
      map.putAll(config.getAsMap());
    }
    return new CompositeState(oldState.configs, map, oldState.holders);
  }

  private CompositeState buildNewState(List<Config> configs) {
    configs = Collections.unmodifiableList(new ArrayList<>(configs));
    Map<String, String> map = new HashMap<>();
    List<Subscription> holders = new ArrayList<>(configs.size());
    for (Config config : configs) {
      map.putAll(config.getAsMap());
      Subscription holder = config.subscribe(ignore -> {
        onChildChange();
      });
      holders.add(holder);
    }
    return new CompositeState(configs, map, holders);
  }

  private static class CompositeState {
    final List<Config> configs;
    final Map<String, String> map;
    final List<Subscription> holders;

    CompositeState(List<Config> configs, Map<String, String> map, List<Subscription> holders) {
      this.configs = configs;
      this.map = map;
      this.holders = holders;
    }
  }
}
