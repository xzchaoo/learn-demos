package com.xzchaoo.learn.db.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.Test;

/**
 * TODO 学习mongodb的各种操作(从以前的项目或笔记里找一下 应该很快就上手了)
 */
public class MongodbTest {
	@Test
	public void test() {
		MongoClientOptions mco = MongoClientOptions.builder()
			.build();
		ServerAddress sa = new ServerAddress(
			"106.14.13.32",
			27777
		);
		MongoClient mc = new MongoClient(
			sa,
			mco
		);
		MongoDatabase md = mc.getDatabase("test");
		//md.createCollection("users");
		MongoCollection<Document> uc = md.getCollection("users2");
		uc.insertOne(new Document("name", "xzc2"));
		System.out.println(uc.count());
	}
}
