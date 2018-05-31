package com.xzchaoo.learn.config.myconfig.configs;

import java.util.Collection;

/**
 * @author xzchaoo
 * @date 2018/5/30
 */
public interface CollectionParser<C extends Collection<? extends T>, T> {
  C parse(String str);
}
