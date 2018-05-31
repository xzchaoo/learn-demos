package com.xzchaoo.learn.config.myconfig.configs;

import java.util.Collection;
import java.util.function.Function;

/**
 * @author xzchaoo
 * @date 2018/5/30
 */
public interface CollectionParser<T, C extends Collection<? super T>> extends Function<String, C> {
}
