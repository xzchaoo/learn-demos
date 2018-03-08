package com.xzchaoo.learn.db.mongodb.async;

import com.mongodb.ConnectionString;
import com.mongodb.async.SingleResultCallback;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;

import org.bson.Document;
import org.junit.Test;

/**
 * created by xzchaoo at 2017/12/20
 *
 * @author xzchaoo
 */
public class MongoAsyncTest {
	@Test
	public void test() {
		new ConnectionString("");
		MongoClient mc = MongoClients.create("mongodb://106.14.136.59:27777");
		MongoDatabase db = mc.getDatabase("test");
		MongoCollection<Document> coll = db.getCollection("user");
		coll.insertOne(new Document("name", "xzc"), new SingleResultCallback<Void>() {
			@Override
			public void onResult(Void aVoid, Throwable throwable) {

			}
		});
	}
}
