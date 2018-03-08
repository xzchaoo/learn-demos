package com.xzchaoo.learn.db.mongodb;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.xzchaoo.learn.db.mongodb.entity.FlightEntity1;
import com.xzchaoo.learn.db.mongodb.entity.MorphiaSaveWithoutAnyIndexEntity;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.InsertOptions;
import org.mongodb.morphia.Morphia;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * 结论
 * 1. Morphia 的 原生 batch save 没有使用 insertMany 语法, 而是单条insert或update, 导致效率非常低, 建议直接使用驱动的insertMany语法
 * 1. 可以使用Morphia的Mapper来帮忙将对象转成DBObbject
 * 2. getCollection("", DBObject.class);
 * created by zcxu at 2017/10/25
 *
 * @author zcxu
 */
public class RSPerformanceTest {

    private MongoClient mc;
    private Morphia morphia;
    private Datastore datastore;
    private MetricRegistry metrics;
    private List<String[]> cityParis;
    private String[] engines = new String[]{"Ctrip", "Amadeus", "SharedPlatform", "CharterPlatform", "Spring"};
    private MongoDatabase database;

    @Before
    public void before() throws Exception {
        metrics = new MetricRegistry();
        String mongodbUri = "mongodb://10.2.72.131:27017,10.2.72.135:27017,10.2.72.186:27017";
        MongoClientOptions.Builder optionsBuilder = new MongoClientOptions.Builder()
            .socketKeepAlive(true)
            //.writeConcern(WriteConcern.UNACKNOWLEDGED)
            ;
        mc = new MongoClient(new MongoClientURI(mongodbUri, optionsBuilder));
        morphia = new Morphia();
        datastore = morphia.createDatastore(mc, "p");
        database = mc.getDatabase("p");
        cityParis = Files.readAllLines(Paths.get(getClass().getClassLoader().getResource("cityPairs.txt").toURI())).stream().map(x -> x.split("\\s")).filter(x -> x.length == 2).collect(Collectors.toList());
    }

    @After
    public void after() {
        mc.close();
    }

    @Test
    public void test_morphia_batchSave_withoutAnyIndex() throws Exception {
        morphia.map(MorphiaSaveWithoutAnyIndexEntity.class);

        //测量值 会统计 每分钟 每五分钟 每十五分钟的频率
        Meter opCount = metrics.meter("requests");
        //统计百分位数 平均值 方差 标准差 这个没有时间限制吗
        Histogram opDuration = metrics.histogram(name(MongoDriverTest.class, "duration"));

        //汇报器
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build();
        reporter.start(1, TimeUnit.SECONDS);

        datastore.delete(datastore.createQuery(MorphiaSaveWithoutAnyIndexEntity.class));
        int threads = 4;
        int loop = 10;
        int batchSize = 100;
        ExecutorService es = Executors.newFixedThreadPool(threads);
        for (int k = 0; k < threads; ++k) {
            es.execute(() -> {
                for (int i = 0; i < loop; ++i) {
                    int ii = i;
                    List<MorphiaSaveWithoutAnyIndexEntity> list = IntStream.range(0, batchSize).mapToObj(j -> {
                        MorphiaSaveWithoutAnyIndexEntity entity = new MorphiaSaveWithoutAnyIndexEntity();
                        entity.setValue("random-value : " + ii + "," + j);
                        return entity;
                    }).collect(Collectors.toList());
                    long begin = System.currentTimeMillis();
                    InsertOptions options = new InsertOptions();
                    datastore.save(list, options);
                    long end = System.currentTimeMillis();
                    opCount.mark();
                    opDuration.update(end - begin);
                }
            });
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.DAYS);
    }

    @Test
    public void test_mongoJavaDriver_batchSave_withoutAnyIndex() throws Exception {
        morphia.map(MorphiaSaveWithoutAnyIndexEntity.class);

        //测量值 会统计 每分钟 每五分钟 每十五分钟的频率
        Meter opCount = metrics.meter("requests");
        //统计百分位数 平均值 方差 标准差 这个没有时间限制吗
        Histogram opDuration = metrics.histogram(name(MongoDriverTest.class, "duration"));

        //汇报器
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build();
        reporter.start(1, TimeUnit.SECONDS);

        datastore.delete(datastore.createQuery(MorphiaSaveWithoutAnyIndexEntity.class));
        MongoCollection<Document> coll = mc.getDatabase("p").getCollection("MorphiaSaveWithoutAnyIndexEntity");
        int threads = 4;
        int loop = 10;
        int batchSize = 100;
        ExecutorService es = Executors.newFixedThreadPool(threads);
        for (int k = 0; k < threads; ++k) {
            es.execute(() -> {
                for (int i = 0; i < loop; ++i) {
                    int ii = i;
                    List<Document> list = IntStream.range(0, batchSize).mapToObj(j -> {
                        return new Document("value", "random-value : " + ii + "," + j);
                    }).collect(Collectors.toList());
                    long begin = System.currentTimeMillis();
                    coll.insertMany(list);
                    long end = System.currentTimeMillis();
                    opCount.mark();
                    opDuration.update(end - begin);
                }
            });
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.DAYS);
    }

    @Test
    public void test_morphia_batchSave_FlightEntity1() throws Exception {
        morphia.map(FlightEntity1.class);
        MongoCollection<DBObject> coll = database.getCollection("FlightEntity1", DBObject.class);
        //测量值 会统计 每分钟 每五分钟 每十五分钟的频率
        Meter opCount = metrics.meter("requests");
        //统计百分位数 平均值 方差 标准差 这个没有时间限制吗
        Histogram opDuration = metrics.histogram(name(MongoDriverTest.class, "duration"));

        //汇报器
        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
            .convertRatesTo(TimeUnit.SECONDS)
            .convertDurationsTo(TimeUnit.MILLISECONDS)
            .build();
        reporter.start(1, TimeUnit.SECONDS);

        datastore.delete(datastore.createQuery(FlightEntity1.class));
        int threads = 4;
        int loop = 10;
        int batchSize = 100;
        ExecutorService es = Executors.newFixedThreadPool(threads);
        LocalDate today = LocalDate.now();
        for (int k = 0; k < threads; ++k) {
            es.execute(() -> {
                Random r = new Random();
                for (int i = 0; i < loop; ++i) {
                    List<DBObject> list = IntStream.range(0, batchSize).mapToObj(j -> {
                        FlightEntity1 fe1 = new FlightEntity1();
                        int cityPairIndex = r.nextInt(cityParis.size());
                        //TODO random
                        fe1.setDepartureCityCode(cityParis.get(cityPairIndex)[0]);
                        fe1.setArrivalCityCode(cityParis.get(cityPairIndex)[1]);
                        fe1.setDepartureCountryId(1);
                        fe1.setArrivalCountryId(1);
                        LocalDate outboundDate = today.plusDays(r.nextInt(3));
                        fe1.setInboundDate(outboundDate);
                        fe1.setTripType("OW");
                        fe1.setDuration(r.nextInt(300));
                        fe1.setDistance(r.nextInt(300));
                        fe1.setSalePrice(r.nextInt(10000));
                        fe1.setTotalPrice(r.nextInt(10000));
                        fe1.setSourceType("B");
                        fe1.setStatus("A");
                        fe1.setNonStop(r.nextBoolean());
                        fe1.setEngine(engines[r.nextInt(engines.length)]);
                        byte[] data = new byte[r.nextInt(3000)];
                        r.nextBytes(data);
                        fe1.setData(data);
                        return morphia.getMapper().toDBObject(fe1);
                    }).collect(Collectors.toList());
                    long begin = System.currentTimeMillis();
                    coll.insertMany(list);
                    long end = System.currentTimeMillis();
                    opCount.mark();
                    opDuration.update(end - begin);
                }
            });
        }
        es.shutdown();
        es.awaitTermination(1, TimeUnit.DAYS);
    }
}
