package com.xzchaoo.learn.db.mongodb;


import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.netty.NettyStreamFactoryFactory;
import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.MongoCollection;
import com.mongodb.rx.client.MongoDatabase;

import org.bson.Document;
import org.junit.Test;

import java.util.Arrays;

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

        //"mongodb://10.2.72.131:27017,10.2.72.135:27017"
        ClusterSettings clusterSettings = ClusterSettings.builder()
            .hosts(Arrays.asList(
                new ServerAddress("10.2.72.131"),
                new ServerAddress("10.2.72.135")
            ))
            .build();
        MongoClientSettings settings = MongoClientSettings.builder()
            .clusterSettings(clusterSettings)
            //如果喜欢用netty
            .streamFactoryFactory(NettyStreamFactoryFactory.builder().build())
            .readPreference(ReadPreference.secondaryPreferred())
            .build();
        MongoClient mc = MongoClients.create(settings);
        MongoDatabase database = mc.getDatabase("test");
        MongoCollection<Document> coll = database.getCollection("user");
        coll.insertOne(new Document("name", "abc")).subscribe(e -> {
            System.out.println("成功");
            mc.close();
        }, Throwable::printStackTrace);
        //睡觉
        Thread.sleep(1000);
    }
}
