package com.xzchaoo.learn.other.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Test;

import java.util.Properties;

/**
 * created by zcxu at 2017/10/27
 *
 * @author zcxu
 */
public class ProducerTest {
	@Test
	public void test1() throws InterruptedException {

		Properties props = new Properties();
		props.put("bootstrap.servers", "106.14.13.32:19092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("batch.size", 16384);
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		Producer<String, String> producer = new KafkaProducer<>(props);
		for (int i = 0; i < 100; i++) {
			System.out.println(i);
			ProducerRecord<String, String> pr = new ProducerRecord<>("test", null, Integer.toString(i));
			producer.send(pr, new Callback() {
				@Override
				public void onCompletion(RecordMetadata metadata, Exception exception) {
					if (exception != null) {
						exception.printStackTrace();
					} else {
						//System.out.println(metadata);
					}
				}
			});
			Thread.sleep(1000);
		}
		producer.flush();
		producer.close();
	}
}
