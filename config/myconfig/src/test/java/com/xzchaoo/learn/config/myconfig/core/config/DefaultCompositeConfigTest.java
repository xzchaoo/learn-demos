package com.xzchaoo.learn.config.myconfig.core.config;


import com.xzchaoo.learn.config.myconfig.core.Config;
import com.xzchaoo.learn.config.myconfig.core.ConfigChangeListener;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/5/25
 */
public class DefaultCompositeConfigTest {
  @Test
  public void test() {
    Map<String, String> map1 = new HashMap<>();
    map1.put("a", "1");
    map1.put("b", "2");
    MapConfig mc1 = new MapConfig(map1);

    Map<String, String> map2 = new HashMap<>();
    map2.put("b", "22");
    map2.put("c", "3");
    MapConfig mc2 = new MapConfig(map2);

    DefaultCompositeConfig dcc = DefaultCompositeConfig.builder()
      .withName("dcc")
      .addConfig(mc1)
      .addConfig(mc2)
      .build();

    dcc.addListener(new ConfigChangeListener() {
      @Override
      public void onConfigChange(Config config) {
        assertThat(config.getInteger("a")).isEqualTo(1);
        assertThat(config.getInteger("b")).isEqualTo(22);
        assertThat(config.getInteger("c")).isEqualTo(3);
        System.out.println("配置变化了");
      }
    });

    assertThat(dcc.getString("d")).isNull();

    map1.put("d", "4");
    mc1.replace(map1);
    assertThat(dcc.getInteger("d")).isEqualTo(4);

    System.out.println(dcc);
  }
}
