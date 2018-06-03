package com.xzchaoo.learn.config.myconfig.core.property;


import com.xzchaoo.learn.config.myconfig.core.parser.DefaultParserProvider;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xzchaoo
 * @date 2018/6/3
 */
public class TestConfigTest {
  @Test
  public void test() {
    TestConfig tc = new TestConfig(new DefaultParserProvider());

    Map<String, String> map = new HashMap<>();
    map.put("a", "1");
    tc.replace(map);

    PropertyContainer<Integer> p = tc.getProperty("a", Integer.class, 0);

    map.put("a", "2");
    tc.replace(map);

    System.out.println(p.get());
  }
}