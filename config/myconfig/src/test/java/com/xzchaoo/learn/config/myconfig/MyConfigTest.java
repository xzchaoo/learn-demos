package com.xzchaoo.learn.config.myconfig;

import com.xzchaoo.learn.config.myconfig.config.CompositeConfig;
import com.xzchaoo.learn.config.myconfig.config.MapConfig;
import com.xzchaoo.learn.config.myconfig.core.Config;
import com.xzchaoo.learn.config.myconfig.core.ConfigObserver;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/5/25
 */
public class MyConfigTest {
  @Test
  public void test_1() {
    Map<String, String> map = new HashMap<>();
    map.put("a", "1");
    map.put("b", "2");
    MapConfig mc = new MapConfig(map);
    assertThat(mc.getInt("a")).isEqualTo(1);
    assertThat(mc.getInt("b")).isEqualTo(2);
  }

  @Test
  public void test_2() {
    CompositeConfig cc = new CompositeConfig();

    Map<String, String> map1 = new HashMap<>();
    map1.put("a", "1");
    map1.put("b", "2");
    MapConfig mc1 = new MapConfig(map1);

    Map<String, String> map2 = new HashMap<>();
    map2.put("b", "22");
    map2.put("c", "3");
    MapConfig mc2 = new MapConfig(map2);

    cc.subscribe(new ConfigObserver() {
      @Override
      public void onChange(Config config) {
        assertThat(config.getInt("a")).isEqualTo(1);
        assertThat(config.getInt("b")).isEqualTo(22);
        assertThat(config.getInt("c")).isEqualTo(3);
        System.out.println("配置变化了");
      }
    });
    cc.replace(asList(mc1, mc2));
    assertThat(cc.getString("d")).isNull();

    map1.put("d", "4");
    mc1.replace(map1);
    assertThat(cc.getInt("d")).isEqualTo(4);
  }
}
