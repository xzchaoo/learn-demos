package com.xzchaoo.learn.config.myconfig.core.property;


import com.xzchaoo.learn.config.myconfig.core.config.MapConfig;
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

    Map<String, String> map = new HashMap<>();
    map.put("a", "1");

    MapConfig mc = new MapConfig(map);

    ConfigBasedPropertySource ps = new ConfigBasedPropertySource(mc, new DefaultParserProvider());

    Property<Integer> p = ps.getProperty("a", Integer.class, 0);
    System.out.println(p.get());

    p.addListener(new PropertyChangeListener<Integer>() {
      @Override
      public void onPropertyChange(Property<Integer> property, Integer oldValue, Integer newValue) {
        System.out.println("配置发生变化 " + property + " " + oldValue + " " + newValue);
      }
    });

    map.put("a", "2");
    mc.replace(map);

    System.out.println(p.get());
  }
}