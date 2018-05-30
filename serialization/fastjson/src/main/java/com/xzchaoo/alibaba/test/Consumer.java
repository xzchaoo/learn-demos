package com.xzchaoo.alibaba.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xzchaoo
 * @date 2018/5/29
 */
public class Consumer extends Thread {
  private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

  private final BlockingQueue<Object> queue;

  public Consumer(BlockingQueue<Object> queue) {
    this.queue = queue;
    setName("Consumer");
  }

  @Override
  public void run() {
    Thread currentThread = Thread.currentThread();
    while (!currentThread.isInterrupted()) {
      try {
        Object e = queue.poll();
        if (e == null) {
          break;
        }
        process(e);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        LOGGER.warn("Consumer thread is interrupted", e);
        break;
      } catch (Exception e) {
        LOGGER.error("process error", e);
      }
    }
  }

  private void process(Object e) {
    LOGGER.info("洗盘子 {}", e);
  }
}
