package com.xzchaoo.learn.config.myconfig.core.config;

import com.ctrip.flight.intl.config.core.CompositeConfig;
import com.ctrip.flight.intl.config.core.Config;
import com.ctrip.flight.intl.config.core.ConfigChangeListener;
import com.google.common.base.Preconditions;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

/**
 * 具备复合能力的Config
 *
 * @author xzchaoo
 * @date 2018/5/25
 */
public class DefaultCompositeConfig extends AbstractConfig implements CompositeConfig {
  private static final State EMPTY = new State(emptyList());

  public static class Builder {
    private List<Config> configs = new ArrayList<>();

    public DefaultCompositeConfig build() {
      return new DefaultCompositeConfig(null, configs);
    }

    public Builder addConfig(Config config) {
      this.configs.add(config);
      return this;
    }
  }

  public static Builder builder() {
    return new Builder();
  }

  /**
   * 内部状态, 是一个不可变对象, 持有合并后的配置
   */
  private volatile State state;

  /**
   * 配置监听器
   */
  private final ConfigChangeListener listener = this::onChildConfigChange;

  public DefaultCompositeConfig(String name) {
    super(name);
    this.state = EMPTY;
  }

  public DefaultCompositeConfig(String name, List<Config> configs) {
    super(name);
    Preconditions.checkNotNull(configs);
    this.state = new State(configs);
    configs.forEach(c -> c.addListener(listener));
  }

  /**
   * 替换内部配置, 必须用锁来保证有序性
   *
   * @param originalConfigs
   */
  @Override
  public synchronized void replaceConfigs(List<Config> originalConfigs) {
    Preconditions.checkNotNull(originalConfigs);
    List<Config> unmodifiableConfigList = Collections.unmodifiableList(new ArrayList<>(originalConfigs));

    // 1. 保存旧状态
    State oldState = this.state;

    // 2. 构建新状态
    State newState = new State(unmodifiableConfigList);

    // 3. 卸载部分监听器
    for (Config config : oldState.configs) {
      if (!newState.configSet.contains(config)) {
        config.removeListener(listener);
      }
    }

    // 4. 添加新监听器
    for (Config config : newState.configs) {
      if (!oldState.configSet.contains(config)) {
        config.addListener(listener);
      }
    }

    // 5. 通知变化
    notifyChanged();
  }

  private synchronized void onChildConfigChange(Config childConfig) {
    State oldState = this.state;
    if (!oldState.configSet.contains(childConfig)) {
      return;
    }
    this.state = new State(oldState);

    notifyChanged();
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
  public Map<String, String> asMap() {
    return this.state.map;
  }

  @Override
  public boolean contains(String key) {
    return this.state.map.containsKey(key);
  }

  @Override
  public List<Config> getConfigs() {
    return this.state.configs;
  }

  private static class State {
    final IdentitySet<Config> configSet;
    final List<Config> configs;
    final Map<String, String> map;

    State(List<Config> configs) {
      this.configs = configs;
      Map<String, String> map = new HashMap<>();
      configSet = new IdentitySet<>();
      for (Config config : configs) {
        map.putAll(config.asMap());
        configSet.add(config);
      }
      this.map = Collections.unmodifiableMap(map);
    }

    State(State oldState) {
      this.configs = oldState.configs;
      this.configSet = oldState.configSet;
      Map<String, String> map = new HashMap<>();
      for (Config config : configs) {
        map.putAll(config.asMap());
      }
      this.map = Collections.unmodifiableMap(map);
    }
  }

  static class IdentitySet<E> extends AbstractSet<E> {
    private final IdentityHashMap<E, Boolean> map = new IdentityHashMap<>();

    @Override
    public Iterator<E> iterator() {
      return map.keySet().iterator();
    }

    @Override
    public boolean add(E e) {
      Boolean value = map.put(e, Boolean.TRUE);
      return value == null;
    }

    @Override
    public boolean contains(Object o) {
      return map.containsKey(o);
    }

    @Override
    public int size() {
      return map.size();
    }
  }

}
