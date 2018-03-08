package com.xzchaoo.learn.jdk8;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author zcxu
 * @date 2018/1/4
 */
public class CollectionTest {
  @Test
  public void test_EnumMap() {
    //如果已Enum为Key那么可以进行优化 底层会使用 数组来存储value 用enum的数值作为下标
  }

  //通过keySet是可以删除key的!
  @Test
  public void test_map_keySet_remove() {
    Map<String, String> map = new HashMap<>();
    map.put("a", "b");
    map.keySet().remove("a");
    assertEquals(0, map.size());
  }
}
