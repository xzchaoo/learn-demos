package com.xzchaoo.learn.db.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Arrays;

/**
 * @author zcxu
 */
public class RSTest {
    @Test
    public void test() {
        String mongodbUri = "mongodb://10.2.72.131:27017,10.2.72.135:27017,10.2.72.186:27017";
        MongoClientOptions.Builder optionsBuilder = new MongoClientOptions.Builder()
            .socketKeepAlive(true)
            .socketTimeout(30000)
            .readPreference(ReadPreference.secondaryPreferred())
            .writeConcern(WriteConcern.W1);
        MongoClient mc = new MongoClient(new MongoClientURI(mongodbUri, optionsBuilder));
        Morphia morphia = new Morphia();
        morphia.map(UniqueObject1.class);
        Datastore datastore = morphia.createDatastore(mc, "unique-key-test");
        datastore.ensureIndexes();
        UniqueObject1 uo1 = new UniqueObject1();
        uo1.setId(new ObjectId("59edb79110841e2638fb9f76"));
        uo1.setName("a");
        uo1.setValue(1);
        datastore.save(Arrays.asList(uo1));

        MongoCollection<Document> coll = mc.getDatabase("test").getCollection("test");
        //注意 morphia 的 save(list) 并没有使用底层的 insertMany 方法 而是一条一条save
        //每次save 会判断id是否存在 如果id存在则会认为这是一条update操作! 千万注意
        //coll.insertMany();
        //coll.insertMany();
    }
}
