package com.xzchaoo.learn.rabbitmq;

import com.rabbitmq.client.Channel;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author xzchaoo
 * @date 2018/4/5
 */
@Configuration
@EnableAsync
@EnableScheduling
public class SpringRabbitConfiguration {

  @Bean
  public ThreadPoolTaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor te = new ThreadPoolTaskExecutor();
    te.setCorePoolSize(12);
    te.setMaxPoolSize(64);
    return te;
  }

  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public Queue step3Queue() {
    return new Queue("step3", false, false, false, null);
  }

  @Bean
  public Queue testSimpleQueue() {
    return new Queue("test.simple", false, false, false, null);
  }

  @Bean
  public Queue testSimple2Queue() {
    return new Queue("test.simple2", false, false, false, null);
  }

  @Bean
  public Queue testSimple5Queue() {
    return new Queue("test.simple5", false, false, false, null);
  }

  @Bean
  public Exchange testSimple4Exchange() {
    return ExchangeBuilder.fanoutExchange("test.simple4")
      .build();
  }

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  //@Bean
  public SimpleMessageListenerContainer mySimpleMessageListenerContainer(ConnectionFactory connectionFactory, MessagePojoListener listener) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueues(testSimpleQueue());
    container.setMessageListener(new MessageListenerAdapter(listener));
    //container.setConsumerArguments(Collections.singletonMap("x-priority", 10));
    return container;
  }

}
