package com.xzchaoo.learn.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import org.junit.Test;

import java.io.IOException;

/**
 * @author xzchaoo
 * @date 2018/4/4
 */
public class RabbitmqExample1 {
  @Test
  public void test_consumer() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("106.14.134.234");
    factory.setUsername("xzc");
    factory.setPassword("xzc");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare("test", false, false, false, null);

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
        throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + message + "'");
      }
    };
    channel.basicConsume("test", true, consumer);

    Thread.sleep(10000);

    // channel.close();
    // connection.close();
  }

  @Test
  public void test_producer() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("106.14.134.234");
    factory.setUsername("xzc");
    factory.setPassword("xzc");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare("test", false, false, false, null);
    for (int i = 0; i < 10; ++i) {
      String message = "Hello World!";
      channel.basicPublish("", "test", null, message.getBytes());
      System.out.println(" [x] Sent '" + message + "'");
      Thread.sleep(1000);
    }

    channel.close();
    connection.close();
  }
}
