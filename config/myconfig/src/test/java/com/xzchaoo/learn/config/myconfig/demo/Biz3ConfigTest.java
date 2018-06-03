package com.xzchaoo.learn.config.myconfig.demo;

import com.xzchaoo.learn.config.myconfig.core.config.MapConfig;
import com.xzchaoo.learn.config.myconfig.core.parser.ConfigProxy;
import com.xzchaoo.learn.config.myconfig.core.parser.ConfigProxyFactory;

import org.assertj.core.data.Offset;
import org.junit.Before;
import org.junit.Test;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author xzchaoo
 * @date 2018/5/31
 */
public class Biz3ConfigTest {

  private MapConfig config;
  private Map<String, String> configMap;

  /**
   * 构建出一个config
   */
  @Before
  public void before() {
    configMap = new HashMap<>();
    configMap.put("biz3.astring", "1");
    configMap.put("biz3.aint", "2");
    configMap.put("biz3.afloat", "3.45");
    configMap.put("biz3.adouble", "6.78");
    configMap.put("biz3.along", "9");
    configMap.put("biz3.aboolean1", "false");
    configMap.put("biz3.aboolean2", "true");
    configMap.put("biz3.stringlist", "1|2|3");
    configMap.put("biz3.map1", "a:1|b:2");
    config = new MapConfig(configMap);
  }

  @Test
  public void test() {
    ConfigProxy configProxy = ConfigProxyFactory.from(config);

    // 返回的是单例
    Biz3Config bc = configProxy.getConfig(Biz3Config.class);
    Biz3Config bc2 = configProxy.getConfig(Biz3Config.class);
    assertThat(bc).isSameAs(bc2);


    assertThat(bc.getAstring()).isEqualTo("1");
    assertThat(bc.getAint()).isEqualTo(2);
    assertThat(bc.getAfloat()).isCloseTo(3.45f, Offset.offset(0.001f));
    assertThat(bc.getAdouble()).isCloseTo(6.78, Offset.offset(0.001));
    assertThat(bc.getAlong()).isEqualTo(9);
    assertThat(bc.isAboolean1()).isEqualTo(false);
    assertThat(bc.isAboolean2()).isEqualTo(true);
    assertThat(bc.getAint2()).isEqualTo(123);
    assertThat(bc.getStringList()).containsOnly("1", "2", "3");
    assertThat(bc.getMap1()).containsEntry("a", 1).containsEntry("b", 2);
    assertThat(bc.getSet1()).containsOnly(new Point(1, 2));


    // 改变属性 断言属性发生变化
    configMap.put("biz3.astring", "change");
    configMap.put("biz3.aint", "2");
    configMap.put("biz3.aint2", "3");
    configMap.put("biz3.afloat", "4.56");
    configMap.put("biz3.adouble", "7.89");
    configMap.put("biz3.along", "1234567890");
    configMap.put("biz3.aboolean1", "true");
    configMap.put("biz3.aboolean2", "false");
    configMap.put("biz3.stringlist", "1|2|3|4");
    configMap.put("biz3.map1", "a:1|b:2|c:3");
    configMap.put("biz3.set1", "3,4|5,6");

    config.replace(configMap);

    assertThat(bc.getAstring()).isEqualTo("change");
    assertThat(bc.getAint()).isEqualTo(2);
    assertThat(bc.getAint2()).isEqualTo(3);
    assertThat(bc.getAfloat()).isCloseTo(4.56f, Offset.offset(0.001f));
    assertThat(bc.getAdouble()).isCloseTo(7.89, Offset.offset(0.001));
    assertThat(bc.getAlong()).isEqualTo(1234567890);
    assertThat(bc.isAboolean1()).isEqualTo(true);
    assertThat(bc.isAboolean2()).isEqualTo(false);
    assertThat(bc.getStringList()).containsOnly("1", "2", "3", "4");
    assertThat(bc.getMap1()).containsEntry("a", 1).containsEntry("b", 2).containsEntry("c", 3);
    assertThat(bc.getSet1()).containsOnly(new Point(3, 4), new Point(5, 6));
  }
}