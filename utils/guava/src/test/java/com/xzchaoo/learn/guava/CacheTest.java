/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.xzchaoo.learn.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class CacheTest {
  @Test
  public void test_weakKeys() throws Exception {
    Cache<Integer, Integer> c = CacheBuilder.newBuilder()
      // 弱key会被移除
      .weakKeys()
      .build();
    c.put(1, 1);
    assertEquals(1, c.size());
    c.put(2, 2);
    assertEquals(2, c.size());
    c.put(3, 3);
    assertEquals(3, c.size());
    c.put(1111, 2222);
    System.gc();
    Thread.sleep(1000);
    c.cleanUp();
    System.out.println(c.size());
  }

  @Test
  public void lazyInitMap() throws Exception {
    //一个懒初始化的缓存
    LoadingCache<Integer, Integer> lc = CacheBuilder.newBuilder()
      .expireAfterAccess(100, TimeUnit.MILLISECONDS)
      .build(new CacheLoader<Integer, Integer>() {
        @Override
        public Integer load(Integer key) throws Exception {
          System.out.println("init " + key);
          Thread.sleep(1000);
          return key;
        }
      });
    new Thread(() -> {
      try {
        System.out.println(lc.get(1));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).start();
    new Thread(() -> {
      try {
        System.out.println(lc.get(1));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }).start();
    Thread.sleep(2000);
    System.out.println(lc.getIfPresent(1));
    System.out.println(lc.getUnchecked(1));
    // 将缓存转成并发Map
    // lc.asMap();
    System.out.println(lc.getIfPresent(4));
  }

  @Test
  public void caceh_3() throws Exception {
    Cache<Integer, Integer> c = CacheBuilder.newBuilder()
      .concurrencyLevel(1)
      .expireAfterWrite(100, TimeUnit.MILLISECONDS)
      .recordStats()
      .build();
    c.put(1, 1);
    assertEquals(1, c.size());
    assertEquals(1, c.getIfPresent(1).intValue());
    Thread.sleep(200);
    //需要手动清理 cache背后并没有线程自动清理
    c.cleanUp();
    assertEquals(0, c.size());
  }

  /**
   * 权重版 这个应该很少用
   *
   * @throws Exception
   */
  @Test
  public void test_weight() throws Exception {
    Cache<Integer, Integer> c = CacheBuilder.newBuilder()
      .maximumWeight(3)
      .recordStats()
      .weigher((Weigher<Integer, Integer>) (key, value) -> key)
      .build();
    c.put(1, 1);
    assertEquals(1, c.size());
    c.put(2, 2);
    assertEquals(2, c.size());
    c.put(3, 3);
    assertEquals(1, c.size());
    //4这个元素根本放不进去 因为权重超过总权重
    c.put(4, 4);
    assertEquals(1, c.size());

    CacheStats cs = c.stats();
    System.out.println(cs);
  }
}
