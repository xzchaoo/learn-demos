package com.xzchaoo.learn.other.kafka;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * created by xzchaoo at 2017/10/27
 *
 * @author xzchaoo
 */
public class ConsumerTest2 {
	@Test
	public void test() {
		Properties props = new Properties();
		props.put("bootstrap.servers", "106.14.175.164:9092");
		props.put("group.id", "test");
		//props.put("enable.auto.commit", "true");
		props.put("enable.auto.commit", "false");
		props.put("auto.commit.interval.ms", "1000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList("test"), new ConsumerRebalanceListener() {
			@Override
			public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
				System.out.println("onPartitionsRevoked");
			}

			@Override
			public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
				System.out.println("onPartitionsAssigned");
			}
		});
		while (true) {
			try {
				ConsumerRecords<String, String> crs = consumer.poll(1000);
				Iterator<ConsumerRecord<String, String>> iter = crs.iterator();
				while (iter.hasNext()) {
					ConsumerRecord<String, String> cr = iter.next();
					System.out.println(cr.value());
				}
				System.out.println(crs.count());
				consumer.commitAsync(new OffsetCommitCallback() {
					@Override
					public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
						System.out.println(offsets);
						if (exception != null) {
							exception.printStackTrace();
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
