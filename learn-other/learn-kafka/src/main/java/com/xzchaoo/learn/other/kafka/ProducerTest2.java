package com.xzchaoo.learn.other.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

import java.util.Properties;

/**
 * created by xzchaoo at 2017/10/27
 *
 * @author xzchaoo
 */
public class ProducerTest2 {
	@Test
	public void test() {
		//提供参数
		Properties props = new Properties();
		props.put("bootstrap.servers", "106.14.175.164:9092");
		props.put("acks", "all");
		props.put("retries", 0);//重试次数
		props.put("batch.size", 16384);//允许的批处理的数据大小
		props.put("linger.ms", 1);//每一毫秒会发送一次batch
		props.put("buffer.memory", 33554432);//发送数据是异步的 这是buffer的大小 如果超过这个大小就会阻塞 如果阻塞超过 max.block.ms 时间 就会抛异常
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		//线程安全
		KafkaProducer<String, String> producer = new KafkaProducer<>(props);
		producer.send(new ProducerRecord<String, String>("test", "a", "b"));
		producer.close();
	}
}
