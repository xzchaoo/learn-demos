package com.xzchaoo.learn.config.myconfig.core.config;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/6/2
 */
public class MapConfigTest {
  @Test
  public void test_1() {
    Map<String, String> map = new HashMap<>();
    map.put("a", "1");
    map.put("b", "2");
    MapConfig mc = new MapConfig(map);
    assertThat(mc.getInteger("a")).isEqualTo(1);
    assertThat(mc.getInteger("b")).isEqualTo(2);
  }

  @Test
  public void test_2() {
    MapConfig mc = MapConfig.builder()
      .put("a", "1")
      .put("b", "2")
      .build();
    assertThat(mc.getInteger("a")).isEqualTo(1);
    assertThat(mc.getInteger("b")).isEqualTo(2);
  }
}