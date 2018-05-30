package com.xzchaoo.alibaba.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xzchaoo
 * @date 2018/5/29
 */
public class Producer extends Thread {
  private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

  /**
   * 阻塞队列
   */
  private final BlockingQueue<Object> queue;

  public Producer(BlockingQueue<Object> queue) {
    this.queue = queue;
    setName("Producer");
  }

  /**
   * 收集下一个可以收集的盘子
   *
   * @return
   */
  protected Object getNextObject() {
    return new Object();
  }

  @Override
  public void run() {
    Thread currentThread = Thread.currentThread();
    while (!currentThread.isInterrupted()) {
      try {
        Object e = getNextObject();
        queue.offer(e);
        LOGGER.info("收集一个盘子放入盘架 {}", e);
      } catch (InterruptedException e) {
        currentThread.interrupt();
        LOGGER.warn("Producer thread is interrupted", e);
        break;
      } catch (Exception e) {
        LOGGER.error("offer error", e);
      }
    }
  }
}
