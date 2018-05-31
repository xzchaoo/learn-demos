package com.xzchaoo.learn.config.myconfig.configs;

import java.util.Map;
import java.util.function.Function;

/**
 * @author xzchaoo
 * @date 2018/5/30
 */
public interface MapParser<K, V, M extends Map<K, V>> extends Function<String, M> {
}
