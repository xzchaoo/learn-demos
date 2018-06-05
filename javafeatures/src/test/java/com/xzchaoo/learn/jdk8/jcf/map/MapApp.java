package com.xzchaoo.learn.jdk8.jcf.map;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Administrator on 2017/3/21.
 */
public class MapApp {
  @Test
  public void test_unmodifiableMap() {
    // unmodifiableMap 为原始的map创建了一个只读视图
    // 对原来map1的修改, map2可以读到修改后的值
    // 由于map只会在扩容的时候有死循环的风险, 因此这里不会有死循环的风险, 但会有其它可见性的问题

    Map<String, String> map1 = new HashMap<>();
    Map<String, String> map2 = Collections.unmodifiableMap(map1);
    map1.put("a", "1");
    System.out.println(map2);
  }

  // 并不会卡死
  @Test
  public void test3() throws InterruptedException {
    Map<String, Boolean> map = new HashMap<>();
    map.put("stop", false);
    Thread t1 = new Thread(() -> {
      long sum = 0;
      for (int i = 0; i < 1000_0000; ++i) {
        sum += i;
      }
      System.out.println("sum=" + sum);
      map.put("stop", true);
    });
    Thread t2 = new Thread(() -> {
      while (!map.get("stop")) {
      }
      System.out.println("t2 finished");
    });
    t1.start();
    t2.start();
    t1.join();
    t2.join();
  }

  @Test
  public void test2() throws InterruptedException {
    Map<String, String> map1 = new HashMap<>();
    Map<String, String> map2 = Collections.unmodifiableMap(map1);
    List<Thread> threadList = new ArrayList<>();

    threadList.add(new Thread(() -> {
      ThreadLocalRandom random = ThreadLocalRandom.current();
      for (int i = 0; i < 300_0000; ++i) {
        String key = Integer.toString(random.nextInt(1000_0000));
        String value = Integer.toString(random.nextInt(1_00));
        map1.put(key, value);
      }
      System.out.println("m finished");
    }));
    for (int j = 0; j < 100; ++j) {
      int jj = j;
      threadList.add(new Thread(() -> {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long sum = 0;
        for (int i = 0; i < 100_0000; ++i) {
          String key = Integer.toString(random.nextInt(1000_0000));
          String value = map2.get(key);
          if (value != null) {
            sum += value.length();
          }
        }
        System.out.println(jj + " finished sum=" + sum);
      }));
    }


    for (Thread t : threadList) {
      t.start();
    }
    for (Thread t : threadList) {
      t.join();
    }
  }

  @Test
  public void test() {
    //JDK8 新增了几个方法 用起来非常方便
    Map<String, Object> map = new HashMap<>();
    //如果不存在才放进去 返回旧值/已有的值
    assertNull(map.putIfAbsent("a", 1));
    assertEquals(1, map.putIfAbsent("a", 1));

    Object c = map.computeIfAbsent("b", key -> {
      assertEquals("b", key);
      return "c";
    });
    assertEquals("c", c);

    c = map.computeIfAbsent("b", key -> {
      assertEquals("b", key);
      return "c2";
    });
    assertEquals("c", c);
  }
}
