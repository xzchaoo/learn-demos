package com.xzchaoo.learn.db.mongodb;


import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.MongoDatabase;
import com.xzchaoo.learn.db.mongodb.mongopojo.codec.LocalDateCodec;
import com.xzchaoo.learn.db.mongodb.mongopojo.codec.LocalDateTimeCodec;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * created by zcxu at 2017/10/26
 *
 * @author zcxu
 */
public class RxMongoDriverTest {
	@Test
	public void test1() throws InterruptedException {
		//所有操作都已经异步化了
		//不要忘记处理异常!

		//构建一个复合的Registry
		CodecRegistry pojoCodecRegistry = fromRegistries(
			com.mongodb.MongoClient.getDefaultCodecRegistry(),//默认的registry

			fromCodecs(new LocalDateCodec(), new LocalDateTimeCodec()),//自定义的registry

			fromProviders(//通过provider提供的registry
				PojoCodecProvider.builder()
					.automatic(true)
					.build())
		);
//		MongoClientOptions.Builder optionBuilder = new MongoClientOptions.Builder()
//			.codecRegistry(pojoCodecRegistry);

		ClusterSettings clusterSettings = ClusterSettings.builder()
			.hosts(Arrays.asList(
				new ServerAddress("106.14.136.59", 27777),
				new ServerAddress("106.14.148.115", 27777),
				new ServerAddress("106.14.134.234", 27777)
			))
			.build();
		MongoClientSettings settings = MongoClientSettings.builder()
			.clusterSettings(clusterSettings)
			.codecRegistry(pojoCodecRegistry)
			//如果喜欢用netty
			//.streamFactoryFactory(NettyStreamFactoryFactory.builder().build())
			.readPreference(ReadPreference.secondaryPreferred())
			.build();
		MongoClient mc = MongoClients.create(settings);
		MongoDatabase database = mc.getDatabase("test");
		MongoCollection<UserPojo> coll = database.getCollection("user", UserPojo.class);
		List<UserPojo> list = new ArrayList<>();
		for (int i = 0; i < 100; ++i) {
			UserPojo up = new UserPojo();
			up.setName("x" + i);
			up.setBirthday(LocalDate.now());
			list.add(up);
		}
		coll.insertMany(list).toBlocking().first();
		System.out.println("成功");
		mc.close();
	}
}
