package com.xzchaoo.learn.config.myconfig.core.parser;


/**
 * @author xzchaoo
 * @date 2018/5/31
 */
class PojoWrapper {
  private final Object pojo;
  private final PojoDescription description;

  PojoWrapper(Object pojo, PojoDescription description) {
    this.pojo = pojo;
    this.description = description;
  }

  Object getPojo() {
    return pojo;
  }

  PojoDescription getDescription() {
    return description;
  }
}
