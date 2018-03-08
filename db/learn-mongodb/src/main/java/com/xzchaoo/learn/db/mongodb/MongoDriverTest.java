package com.xzchaoo.learn.db.mongodb;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.codahale.metrics.MetricRegistry.name;

public class MongoDriverTest {
    //注册中心
    static final MetricRegistry metrics = new MetricRegistry();

    @Test
    public void test1() throws InterruptedException {
        //测量值 会统计 每分钟 每五分钟 每十五分钟的频率
        Meter requests = metrics.meter("requests");
        //统计百分位数 平均值 方差 标准差 这个没有时间限制吗
        Histogram duration = metrics.histogram(name(MongoDriverTest.class, "duration"));

        //汇报器
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build();
        reporter.start(1, TimeUnit.SECONDS);

        //new MongoClientURI()
        ServerAddress serverAddress = new ServerAddress("10.32.129.143", 5356);
        MongoClientOptions options = MongoClientOptions.builder()
            .socketTimeout(10000)
            .connectTimeout(1000)
            .maxWaitTime(30000)
//            .threadsAllowedToBlockForConnectionMultiplier(50)
            .build();
        MongoClient client = new MongoClient(serverAddress, options);
        MongoDatabase db = client.getDatabase("test1");
        MongoCollection<Document> coll = db.getCollection("users");
        Document d = new Document();

        ExecutorService es = Executors.newFixedThreadPool(16);
        IntStream.range(0, 16).forEach(i -> {
            es.execute(() -> {

                while (true) {
                    try {
                        Document doc = new Document("name", "xzc");
                        doc.append("data", "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890".getBytes());
                        long begin = System.currentTimeMillis();
                        coll.insertOne(doc);
                        long end = System.currentTimeMillis();
                        duration.update(end - begin);
                        requests.mark();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        });
        Thread.sleep(10000000);
    }
}
