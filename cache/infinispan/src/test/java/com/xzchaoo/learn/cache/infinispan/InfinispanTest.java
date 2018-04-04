package com.xzchaoo.learn.cache.infinispan;

import org.infinispan.Cache;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.junit.Test;

import java.io.IOException;

import lombok.val;

/**
 * @author xzchaoo
 * @date 2018/4/1
 */
public class InfinispanTest {
  @Test
  public void test() throws IOException, InterruptedException {
    //sdfsdf

    val cm = new DefaultCacheManager("infinispan.xml");
    cm.defineConfiguration("custom", new ConfigurationBuilder()
      .expiration()
      .wakeUpInterval(5000)
      .lifespan(5000)
      .maxIdle(5000)
      .eviction()
      .build());

    cm.start();
    Cache<String, Object> cache = cm.getCache("custom");
    cache.put("a", new Object());
    System.out.println(cache.get("a"));
    for (int i = 0; i < 20; ++i) {
      System.out.println(i + " " + cache.get("a"));
      Thread.sleep(1000);
    }
  }
}
