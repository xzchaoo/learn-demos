package com.xzchaoo.alibaba.test;


/**
 * 基于 synchronized 实现的简单的线程安全队列
 *
 * @author xzchaoo
 * @date 2018/5/29
 */
public class SimpleBlockingQueue<E> implements BlockingQueue<E> {
  /**
   * 用于同步的对象
   */
  private final Object lock = new Object();


  /**
   * 实际存储元素的数组
   */
  private final Object[] buffer;

  /**
   * 数组的mask
   */
  private final int mask;

  /**
   * 最大缓冲住多少元素
   */
  private final int maxSize;

  /**
   * 生产者索引
   */
  private int offerIndex;

  /**
   * 消费者索引
   */
  private int pollIndex;

  private static final int MAX_SIZE = 1 << 30;

  public SimpleBlockingQueue(int maxSize) {
    if (maxSize <= 0 || maxSize > MAX_SIZE) {
      throw new IllegalArgumentException("invalid maxSize " + maxSize);
    }

    this.maxSize = maxSize;
    int arraySize = calculateSuitableSize(maxSize);
    this.mask = arraySize - 1;
    this.buffer = new Object[arraySize];
  }


  /**
   * 找到 >= size 的最小2^n 的数, 作为buffer大小
   *
   * @param size 大小
   * @return 大于等于该size的最小2^n数
   */
  private static int calculateSuitableSize(int size) {
    int leadingZeros = Integer.numberOfLeadingZeros(size);
    int highestBit = 32 - leadingZeros;
    int arraySize;
    if ((1 << (highestBit - 1)) == size) {
      arraySize = size;
    } else {
      arraySize = 1 << highestBit;
    }
    return arraySize;
  }

  @Override
  public boolean offer(E e) throws InterruptedException {
    synchronized (lock) {
      // 一直等 直到有可用位置
      for (; ; ) {
        // 其实只会出现 == 的情况 但还是写成<= 确保安全
        if (pollIndex + maxSize <= offerIndex) {
          lock.wait();
        } else {
          boolean needNotify = pollIndex == offerIndex;
          buffer[(offerIndex++) & mask] = e;
          if (needNotify) {
            lock.notify();
          }
          break;
        }
      }
    }
    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  public E poll() throws InterruptedException {
    synchronized (lock) {
      for (; ; ) {
        if (offerIndex <= pollIndex) {
          lock.wait();
        } else {
          boolean needNotify = pollIndex + maxSize == offerIndex;
          E e = (E) buffer[(pollIndex++) & mask];
          if (needNotify) {
            lock.notify();
          }
          return e;
        }
      }
    }
  }
}