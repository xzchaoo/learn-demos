package com.xzchaoo.learn.jdk8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * HashMap 并发插入时出现死锁问题
 *
 * @author xzchaoo
 * @date 2018/5/27
 */
public class HashMapConcurrentTest {
  @Test
  public void test() throws InterruptedException {
    Map<String, String> map = new HashMap<>();
    List<Thread> list = new ArrayList<>();
    for (int i = 0; i < 100; ++i) {
      int index = i;
      Thread t = new Thread(() -> {
        System.out.println("start " + index);
        Random random = ThreadLocalRandom.current();
        try {
          for (int j = 0; j < 1000000; ++j) {
            int x = random.nextInt(100000);
            map.put(Integer.toString(x), Integer.toString(x));
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        System.out.println("finished " + index);
      });
      list.add(t);
    }
    for (Thread t : list) {
      t.start();
    }
    for (Thread t : list) {
      t.join();
    }
  }
}
