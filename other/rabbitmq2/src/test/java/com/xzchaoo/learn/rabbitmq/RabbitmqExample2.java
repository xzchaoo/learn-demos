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
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xzchaoo
 * @date 2018/4/4
 */
public class RabbitmqExample2 {
  @Test
  public void test_consumer() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare("test", false, false, false, null);

    AtomicInteger ai = new AtomicInteger(0);

    //默认是串行处理
    channel.basicQos(2);
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        new Thread(() -> {
          System.out.println(ai.incrementAndGet());
          try {
            System.out.println(envelope + " " + Thread.currentThread().getName());
            String message = new String(body, "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            Thread.sleep(1000);
          } catch (Exception e) {
            e.printStackTrace();
          } finally {
            ai.decrementAndGet();
            try {
              channel.basicAck(envelope.getDeliveryTag(), false);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }).start();
      }
    };
    channel.basicConsume("test", false, consumer);
    Thread.sleep(10000);

    // channel.close();
    // connection.close();
  }

  @Test
  public void test_producer() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare("test", false, false, false, null);
    for (int i = 0; i < 10; ++i) {
      String message = "msg " + i;
      channel.basicPublish("", "test", null, message.getBytes());
      System.out.println(" [x] Sent '" + message + "'");
      Thread.sleep(1000);
    }

    channel.close();
    connection.close();
  }
}
