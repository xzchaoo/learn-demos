package com.xzchaoo.learn.archaius2;

import com.netflix.archaius.api.Config;
import com.netflix.archaius.config.AbstractConfig;

import java.util.Iterator;

/**
 * created by xzchaoo at 2017/11/20
 *
 * @author xzchaoo
 */
public class ConfigWrapper extends AbstractConfig {
	private Config config;

	public ConfigWrapper(String name, Config config) {
		super(name);
		setConfig(config);
	}

	public ConfigWrapper(Config config) {
		setConfig(config);
	}

	@Override
	public Object getRawProperty(String key) {
		return config.getRawProperty(key);
	}

	@Override
	public boolean containsKey(String key) {
		return config.containsKey(key);
	}

	@Override
	public Iterator<String> getKeys() {
		return config.getKeys();
	}

	@Override
	public boolean isEmpty() {
		return config.isEmpty();
	}

	public synchronized void replaceConfig(Config config) {
		setConfig(config);
		notifyConfigUpdated(this);
	}

	private void setConfig(Config config) {
		if (config == null) {
			throw new IllegalArgumentException();
		}
		this.config = config;
	}
}
