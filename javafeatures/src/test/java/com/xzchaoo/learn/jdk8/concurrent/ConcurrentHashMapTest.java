package com.xzchaoo.learn.jdk8.concurrent;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;

import lombok.val;

/**
 * computeIfAbsent 的创建方法只会被调用一次 其它的会阻塞
 *
 * @author xzchaoo
 * @date 2018/5/29
 */
public class ConcurrentHashMapTest {
  /**
   * 利用 get + putIfAbsent 可以代替 computeIfAbsent 并且是无阻塞的
   */
  @Test
  public void test1() throws InterruptedException {
    val map = new ConcurrentHashMap<Integer, Integer>();
    val t1 = new Thread(() -> {
      Integer value = map.get(1);
      if (value == null) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        value = 1;
        Integer oldValue = map.putIfAbsent(1, value);
        value = oldValue != null ? oldValue : value;
      }
      System.out.println("1 获得value " + value);
    });
    val t2 = new Thread(() -> {
      Integer value = map.get(1);
      if (value == null) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        value = 2;
        Integer oldValue = map.putIfAbsent(1, value);
        value = oldValue != null ? oldValue : value;
      }
      System.out.println("2 获得value " + value);
    });
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    System.out.println(map);
  }

  @Test
  public void test2() throws InterruptedException {
    val map = new ConcurrentHashMap<Integer, Integer>();
    val t1 = new Thread(() -> {
      map.computeIfAbsent(1, key -> {
        System.out.println("1 在执行");
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        return 1;
      });
    });

    val t2 = new Thread(() -> {
      map.computeIfAbsent(1, key -> {
        System.out.println("2 在执行");
        try {
          Thread.sleep(5000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        return 2;
      });
    });
    t1.start();
    t2.start();
    t1.join();
    t2.join();
    System.out.println(map);
  }
}
