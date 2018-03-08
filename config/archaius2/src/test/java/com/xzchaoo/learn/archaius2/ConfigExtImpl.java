package com.xzchaoo.learn.archaius2;

import com.netflix.archaius.ConfigProxyFactory;
import com.netflix.archaius.DefaultDecoder;
import com.netflix.archaius.DefaultPropertyFactory;
import com.netflix.archaius.api.Config;
import com.netflix.archaius.api.Property;
import com.netflix.archaius.api.PropertyContainer;
import com.netflix.archaius.api.PropertyFactory;
import com.netflix.archaius.config.DefaultCompositeConfig;
import com.netflix.archaius.config.DefaultConfigListener;
import com.netflix.archaius.property.DefaultPropertyListener;

import java.util.function.Consumer;

/**
 * created by xzchaoo at 2017/11/20
 *
 * @author xzchaoo
 */
public class ConfigExtImpl extends DefaultCompositeConfig implements ConfigExt {
	private PropertyFactory propertyFactory;
	private ConfigProxyFactory configProxyfactory;

	public void init() {
		propertyFactory = DefaultPropertyFactory.from(this);
		configProxyfactory = new ConfigProxyFactory(this, DefaultDecoder.INSTANCE, propertyFactory);
	}

	@Override
	public PropertyContainer getPropertyContainer(String propertyName) {
		return propertyFactory.getProperty(propertyName);
	}

	@Override
	public PropertyFactory getPropertyFactory() {
		return propertyFactory;
	}

	@Override
	public <T> T newProxy(Class<T> clazz) {
		return configProxyfactory.newProxy(clazz);
	}

	@Override
	public <T> T newProxy(Class<T> clazz, String initialPrefix) {
		return configProxyfactory.newProxy(clazz, initialPrefix);
	}

	@Override
	public <T> void bind(Property<T> property, Consumer<T> consumer) {
		property.addListener(new DefaultPropertyListener<T>() {
			@Override
			public void onChange(T value) {
				consumer.accept(value);
			}
		});
	}

	@Override
	public void bindString(String propertyName, String defaultValue, Consumer<String> consumer) {
		bind(propertyFactory.getProperty(propertyName).asString(defaultValue), consumer);
	}

	@Override
	public void bindString(String propertyName, Consumer<String> consumer) {
		bindString(propertyName, null, consumer);
	}

	@Override
	public void bindInteger(String propertyName, Integer defaultValue, Consumer<Integer> consumer) {
		bind(propertyFactory.getProperty(propertyName).asInteger(defaultValue), consumer);
	}

	@Override
	public void bindInteger(String propertyName, Consumer<Integer> consumer) {
		bindInteger(propertyName, null, consumer);
	}

	@Override
	public void bindBoolean(String propertyName, Boolean defaultValue, Consumer<Boolean> consumer) {
		bind(propertyFactory.getProperty(propertyName).asBoolean(defaultValue), consumer);
	}

	@Override
	public void bindBoolean(String propertyName, Consumer<Boolean> consumer) {
		bindBoolean(propertyName, consumer);
	}

	@Override
	public void bindFloat(String propertyName, Float defaultValue, Consumer<Float> consumer) {
		bind(propertyFactory.getProperty(propertyName).asFloat(defaultValue), consumer);
	}

	@Override
	public void bindFloat(String propertyName, Consumer<Float> consumer) {
		bindFloat(propertyName, null, consumer);
	}

	@Override
	public void bindLong(String propertyName, Long defaultValue, Consumer<Long> consumer) {
		bind(propertyFactory.getProperty(propertyName).asLong(defaultValue), consumer);
	}

	@Override
	public void bindLong(String propertyName, Consumer<Long> consumer) {
		bindLong(propertyName, null, consumer);
	}

	@Override
	public void bindShort(String propertyName, Short defaultValue, Consumer<Short> consumer) {
		bind(propertyFactory.getProperty(propertyName).asShort(defaultValue), consumer);
	}

	@Override
	public void bindShort(String propertyName, Consumer<Short> consumer) {
		bindShort(propertyName, null, consumer);
	}

	@Override
	public void addConfigUpdateListenerAndTriggerNow(Consumer<ConfigExt> consumer) {
		addListener(new DefaultConfigListener() {
			@Override
			public void onConfigUpdated(Config config) {
				consumer.accept(ConfigExtImpl.this);
			}
		});
		consumer.accept(this);
	}
}
