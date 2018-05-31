package com.xzchaoo.learn.config.myconfig.biz.parsers;

import com.xzchaoo.learn.config.myconfig.config.MapConfig;
import com.xzchaoo.learn.config.myconfig.core.Config;

import org.assertj.core.data.Offset;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class Biz3ConfigTest {
  @Test
  public void test() {
    Map<String, String> configMap = new HashMap<>();

    configMap.put("biz3.astring", "ssttrr");
    configMap.put("biz3.aint", "123");
    configMap.put("biz3.afloat", "1.2");
    configMap.put("biz3.adouble", "3.14");
    configMap.put("biz3.along", "123456");
    configMap.put("biz3.aboolean1", "false");
    configMap.put("biz3.aboolean2", "true");
    configMap.put("biz3.stringlist", "1|2|3");
    configMap.put("biz3.map1", "a:1|b:2");

    Config config = new MapConfig(configMap);
    ConfigProxy configProxy = ConfigProxyFactory.from(config);
    Biz3Config bc = configProxy.getConfig(Biz3Config.class);
    assertThat(bc.getAstring()).isEqualTo("ssttrr");
    assertThat(bc.getAint()).isEqualTo(123);
    assertThat(bc.getAfloat()).isCloseTo(1.2f, Offset.offset(0.001f));
    assertThat(bc.getAdouble()).isCloseTo(3.14, Offset.offset(0.001));
    assertThat(bc.getAlong()).isEqualTo(123456);
    assertThat(bc.isAboolean1()).isEqualTo(false);
    assertThat(bc.isAboolean2()).isEqualTo(true);
    assertThat(bc.getAint2()).isEqualTo(123);
    assertThat(bc.getStringList()).containsOnly("1", "2", "3");
    System.out.println(bc.getMap1());
  }
}