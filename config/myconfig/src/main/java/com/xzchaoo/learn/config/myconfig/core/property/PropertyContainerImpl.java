package com.xzchaoo.learn.config.myconfig.core.property;


import com.xzchaoo.learn.config.myconfig.core.Config;
import com.xzchaoo.learn.config.myconfig.core.parser.Parser;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xzchaoo
 * @date 2018/6/3
 */
public class PropertyContainerImpl<T> implements PropertyContainer<T> {
  private final String key;
  private final Parser<T> parser;
  private final T defaultValue;
  private final List<PropertyChangeListener<T>> listeners = new CopyOnWriteArrayList<>();
  // private final List<PropertyContainer<?>> dProperties = new CopyOnWriteArrayList<CachedProperty<?>>();

  private volatile String rawString;
  private volatile T cachedValue;


  public PropertyContainerImpl(String key, Config config, Parser<T> parser, T defaultValue) {
    this.key = key;
    this.parser = parser;
    this.defaultValue = defaultValue;
    onConfigChange(config);
    config.addListener(this::onConfigChange);
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public String getRawString() {
    return rawString;
  }

  @Override
  public T get() {
    return this.cachedValue;
  }

  @Override
  public void addListener(PropertyChangeListener<T> listener) {
    listeners.add(listener);
  }

  @Override
  public void removeListener(PropertyChangeListener<T> listener) {
    listeners.remove(listener);
  }

  private synchronized void onConfigChange(Config config) {
    T oldValue = this.cachedValue;
    String rawString = config.getString(key);
    T parsedValue = parser.parse(rawString);
    T newValue = parsedValue != null ? parsedValue : defaultValue;
    this.rawString = rawString;
    this.cachedValue = newValue;
    if (oldValue != newValue || !oldValue.equals(newValue)) {
      notifyPropertyChange(oldValue, newValue);
    }
  }

  private void notifyPropertyChange(T oldValue, T newValue) {
    for (PropertyChangeListener<T> listener : listeners) {
      listener.onPropertyChange(this, oldValue, newValue);
    }
  }
}
