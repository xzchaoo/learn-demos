package com.xzchaoo.learn.db.mongodb;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.xzchaoo.learn.db.mongodb.codec.LocalDateCodec;
import com.xzchaoo.learn.db.mongodb.codec.LocalDateTimeCodec;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.Test;

import java.util.Arrays;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * created by zcxu at 2017/10/26
 *
 * @author zcxu
 */
public class MongoPOJOTest {
    @Test
    public void test1() {
        //构建一个复合的Registry
        CodecRegistry pojoCodecRegistry = fromRegistries(
            MongoClient.getDefaultCodecRegistry(),//默认的registry

            fromCodecs(new LocalDateCodec(), new LocalDateTimeCodec()),//自定义的registry

            fromProviders(//通过provider提供的registry
                PojoCodecProvider.builder()
                    .automatic(true)
                    .build())
        );
        MongoClientOptions.Builder optionBuilder = new MongoClientOptions.Builder()
            .codecRegistry(pojoCodecRegistry);
        MongoClient mc = new MongoClient(new MongoClientURI("mongodb://10.2.72.131:27017,10.2.72.135:27017", optionBuilder));
        MongoDatabase database = mc.getDatabase("test");
        MongoCollection<UserPojo> coll = database.getCollection("user", UserPojo.class);

        //ClassModel.builder(UserPojo.class).build();

//        UserPojo u = new UserPojo();
//        u.setName("1");
//        u.setBirthday(LocalDate.now());
        //coll.insertOne(u);
        //System.out.println(u);
        //这个query没办法用builder的方式进行构建
        coll.find(
            and(
                eq("name", "1")
            )
        ).forEach((Block<? super UserPojo>) System.out::println);

//        coll.aggregate(Arrays.asList(
//            Aggregates.match(and(eq("status", "A"))),
//            Aggregates.sort(new Document("totalPrice", 1))
//            Aggregates.replaceRoot("sa"),
//            Aggregates.count()
//        ));
    }
}
