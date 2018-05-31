package com.xzchaoo.alibaba.test;

/**
 * @author xzchaoo
 * @date 2018/5/29
 */
public interface BlockingQueue<E> {
  /**
   * 往队尾加入一个元素
   *
   * @param e 元素
   * @return 总是返回true
   * @throws InterruptedException 线程中断异常
   */
  boolean offer(E e) throws InterruptedException;

  /**
   * 阻塞地从队首获取一个元素
   *
   * @return 获取的元素, 约定返回null表示队列不会再产生元素
   * @throws InterruptedException 线程中断异常
   */
  E poll() throws InterruptedException;
}
