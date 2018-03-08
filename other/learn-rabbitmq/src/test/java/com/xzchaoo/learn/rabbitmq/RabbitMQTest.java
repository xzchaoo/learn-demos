package com.xzchaoo.learn.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.junit.Ignore;

/**
 * @author xzchaoo
 * @date 2017/12/25
 */
public class RabbitMQTest {
	@Ignore
	public void test() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
//		factory.setUsername();
//		factory.setPassword();
//		factory.setVirtualHost();
		factory.setHost("106.14.148.115");
		factory.setPort(5672);
		Connection conn = factory.newConnection();
		Channel channel = conn.createChannel();
		channel.exchangeDeclare("test", "direct", true);
		String queueName = channel.queueDeclare().getQueue();
		channel.exchangeBind(queueName, "test", "routeKey");
		channel.close();
		conn.close();
	}
}
