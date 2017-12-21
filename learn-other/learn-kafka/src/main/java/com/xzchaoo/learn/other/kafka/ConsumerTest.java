package com.xzchaoo.learn.other.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

/**
 * created by zcxu at 2017/10/27
 *
 * @author zcxu
 */
public class ConsumerTest {
	@Test
	public void test() {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "106.14.13.32:19092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "test1");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
		KafkaConsumer<String, Long> consumer = new KafkaConsumer<>(props);
		//max.poll.records
		//手动拉模式
		//consumer.subscribe(Arrays.asList("test"));
		consumer.subscribe(Arrays.asList("WordsWithCountsTopic"), new ConsumerRebalanceListener() {
			@Override
			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

			}

			@Override
			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {

			}
		});
		while (true) {
			ConsumerRecords<String, Long> records = consumer.poll(100);
			for (ConsumerRecord<String, Long> record : records) {
				System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
			}
		}
	}
}
