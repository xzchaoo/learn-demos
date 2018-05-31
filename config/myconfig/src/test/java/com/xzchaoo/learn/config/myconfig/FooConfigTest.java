package com.xzchaoo.learn.config.myconfig;

import com.xzchaoo.learn.config.myconfig.foo.FooConfig;

import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class FooConfigTest {

  @Test
  public void test() {
    FooConfig fooConfig = new FooConfig();

    Map<String, String> properties = new HashMap<>();
    properties.put("key.list", "1,2,3,4");
    properties.put("key.map", "a=1|b=2");

    fooConfig.refresh(properties);

    Collection<Integer> list = fooConfig.getList("key.list");
  }
}
