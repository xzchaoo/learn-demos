package com.xzchaoo.learn.rabbitmq;

import org.junit.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * direct routeKey就是queue名来匹配, 一个queue可以有多个消费者, 一个消息只会被一个消费者消费
 * fanout routeKey被忽略 总是广播到当前所有的consumer
 * topic routeKey具有特定的格式, consumer的routeKey支持通配符
 *
 * @author xzchaoo
 * @date 2018/4/5
 */
@SpringBootApplication
public class SpringRabbitTest {
  @Bean
  public ApplicationRunner applicationRunner(ConnectionFactory cf, RabbitTemplate rt, RabbitListenerEndpointRegistry r) {
    return args -> {
      cf.createConnection().close();
      Thread.sleep(1_000);
      System.out.println("启动");
      r.start();
    };
  }

  @Test
  public void test() throws InterruptedException {
    ConfigurableApplicationContext ac = SpringApplication.run(SpringRabbitTest.class);
    AmqpTemplate t = ac.getBean(AmqpTemplate.class);

    // 几种AMQP的概念的spring里的对应类
    // ExchangeBuilder.directExchange("").delayed().build();
    // QueueBuilder.nonDurable().build();
    // 它们只用于持有描述信息 不会引用真正的实现

    // AmqpAdmin 可以用于做修改性的操作

    // BindingBuilder
    for (int i = 0; i < 180; ++i) {
      //t.convertAndSend("", "step1", i);
      // t.convertAndSend("", "test.simple", i);
      t.convertAndSend("", "test.simple2", Integer.toString(i));
      // t.convertAndSend("", "test.simple3", Integer.toString(i));
      // t.convertAndSend("", "test.simple6", Integer.toString(i));
      // t.convertAndSend("", "test.simple7", Integer.toString(i));
      // t.convertAndSend("", "test.simple8", Integer.toString(i));
      // t.convertAndSend("", "test.simple9", Integer.toString(i));
      // t.convertAndSend("", "test.simple10", Integer.toString(i));
      // t.convertAndSend("test.simple4", "", Integer.toString(i));
      // t.convertAndSend("app.simple4", "", Integer.toString(i));
      // // new AsyncRabbitTemplate();
      // MessageEntity e = new MessageEntity();
      // e.setAint(i);
      // e.setAstring(Integer.toString(i));
      // t.convertAndSend("", "test.simple5", e);
      Thread.sleep(1000);
    }
    // Object result = t.receiveAndConvert("test");
    // System.out.println(result);
    Thread.sleep(10000);
  }
}
