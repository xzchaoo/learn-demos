package com.xzchaoo.learn.archaius2;

import com.netflix.archaius.api.Property;
import com.netflix.archaius.api.PropertyContainer;
import com.netflix.archaius.api.PropertyFactory;
import com.netflix.archaius.api.config.CompositeConfig;

import java.util.function.Consumer;

/**
 * created by xzchaoo at 2017/11/20
 *
 * @author xzchaoo
 */
public interface ConfigExt extends CompositeConfig {
	PropertyContainer getPropertyContainer(String propertyName);

	PropertyFactory getPropertyFactory();

	<T> T newProxy(Class<T> clazz);

	<T> T newProxy(Class<T> clazz, String initialPrefix);

	<T> void bind(Property<T> property, Consumer<T> consumer);

	void bindString(String propertyName, String defaultValue, Consumer<String> consumer);

	void bindString(String propertyName, Consumer<String> consumer);

	void bindInteger(String propertyName, Integer defaultValue, Consumer<Integer> consumer);

	void bindInteger(String propertyName, Consumer<Integer> consumer);

	void bindBoolean(String propertyName, Boolean defaultValue, Consumer<Boolean> consumer);

	void bindBoolean(String propertyName, Consumer<Boolean> consumer);

	void bindFloat(String propertyName, Float defaultValue, Consumer<Float> consumer);

	void bindFloat(String propertyName, Consumer<Float> consumer);

	void bindLong(String propertyName, Long defaultValue, Consumer<Long> consumer);

	void bindLong(String propertyName, Consumer<Long> consumer);

	void bindShort(String propertyName, Short defaultValue, Consumer<Short> consumer);

	void bindShort(String propertyName, Consumer<Short> consumer);

	void addConfigUpdateListenerAndTriggerNow(Consumer<ConfigExt> consumer);
}
