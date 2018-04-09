package com.xzchaoo.learn.rabbitmq;

import org.springframework.stereotype.Component;

/**
 * @author xzchaoo
 * @date 2018/4/5
 */
@Component
public class MessagePojoListener {
  public void handleMessage(Integer message) {
    System.out.println("MessagePojoListener 收到消息 " + message);
  }
}
