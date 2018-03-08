package com.xzchaoo.learn.db.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.mongojack.JacksonDBCollection;

/**
 * @author zcxu
 * @date 2017/12/19
 */
public class BulkWriteTest {
	@Test
	public void test() {
		MongoClientOptions.Builder b = new MongoClientOptions.Builder();
		b.readPreference(ReadPreference.secondaryPreferred());
		MongoClientURI uri = new MongoClientURI("mongodb://10.2.72.131:27017", b);
		MongoClient client = new MongoClient(uri);
		MongoDatabase fooDB = client.getDatabase("lp_flight_ow");
		MongoCollection<Document> barColl = fooDB.getCollection("lp_flight");
		//JacksonDBCollection<BlogPost, String> col = JacksonDBCollection.wrap(barColl, FlightEntity.class, ObjectId.class);

//		barColl.bulkWrite(Arrays.asList(
//			new InsertOneModel<>();
//		));

//		barColl.aggregate(Arrays.asList(
//			match(and(eq("departureCityCode", "SHA"), eq("arrivalCityCode", "HKG"))),
//			group("$outboundDate", Accumulators.min("minTotalPrice", "$totalPrice"))
//		)).forEach((Consumer<? super Document>) System.out::println);
	}
}
