package com.xzchaoo.alibaba.test;

import org.junit.Test;

/**
 * 基本思路是生产者消费者模式, 双方需要一个阻塞队里作为传输介质
 * 由于不允许使用现成的实现, 那就自己模拟一个, 性能稍差
 *
 * @author xzchaoo
 * @date 2018/5/29
 */
public class Problem {
  @Test
  public void test() throws InterruptedException {
    BlockingQueue<Object> queue = new SimpleBlockingQueue<>(10);
    Consumer consumer = new Consumer(queue);
    Producer producer = new Producer(queue);
    consumer.start();
    producer.start();
    Thread.sleep(30_000);
    consumer.interrupt();
    producer.interrupt();
    consumer.join();
    producer.join();
    System.out.println("finished");
  }
}
