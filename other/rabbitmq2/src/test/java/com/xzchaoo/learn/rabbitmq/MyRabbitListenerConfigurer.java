package com.xzchaoo.learn.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.stereotype.Component;

/**
 * @author xzchaoo
 * @date 2018/4/5
 */
// @Component
public class MyRabbitListenerConfigurer implements RabbitListenerConfigurer {
  @Override
  public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
    // SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
    // endpoint.setQueueNames("anotherQueue");
    // endpoint.setMessageListener(message -> {
    //   // processing
    // });
    // registrar.registerEndpoint(endpoint);
  }
}
