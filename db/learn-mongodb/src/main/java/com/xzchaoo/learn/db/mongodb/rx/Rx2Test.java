package com.xzchaoo.learn.db.mongodb.rx;

import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import org.bson.Document;
import org.junit.Test;

import java.util.Arrays;

import io.reactivex.Flowable;

/**
 * created by xzchaoo at 2017/12/20
 *
 * @author xzchaoo
 */
public class Rx2Test {
	@Test
	public void test() {
		ClusterSettings clusterSettings = ClusterSettings.builder()
			.hosts(Arrays.asList(
				new ServerAddress("106.14.136.59", 27777),
				new ServerAddress("106.14.148.115", 27777),
				new ServerAddress("106.14.134.234", 27777)
			))
			.build();
		MongoClientSettings settings = MongoClientSettings.builder()
			.clusterSettings(clusterSettings)
			//如果喜欢用netty
			//.streamFactoryFactory(NettyStreamFactoryFactory.builder().build())
			.readPreference(ReadPreference.secondaryPreferred())
			.build();
		MongoClient mc = MongoClients.create(settings);
		MongoDatabase db = mc.getDatabase("test");
		MongoCollection<Document> coll = db.getCollection("user");
		FindPublisher<Document> fp = coll.find();

		//适配!
		Flowable.fromPublisher(fp);

	}
}
