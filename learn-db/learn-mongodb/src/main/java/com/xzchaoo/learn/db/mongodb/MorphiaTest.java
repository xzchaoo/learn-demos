package com.xzchaoo.learn.db.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.InsertOptions;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.MapperOptions;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * mongodb官方出品, 基于 mongo-java-driver
 */
public class MorphiaTest {

	private Morphia morphia;
	private Datastore datastore;

	@Before
	public void before() {
		morphia = new Morphia();

		//映射
		morphia.map(User.class);
		MapperOptions options = new MapperOptions();
		morphia.getMapper().setOptions(options);
		//morphia.mapPackage() 扫包

		//默认情况下M 不会保存空数组 空map 和 null
//		options.setStoreEmpties(true);
//		options.setStoreNulls(true);

		datastore = morphia.createDatastore(new MongoClient("106.14.13.32", 27777), "test");
		datastore.ensureIndexes();
	}

	@Test
	public void test_save() {
		User user = new User();
		user.setUsername("xzc1");
		user.setPassword("xxx");
		user.setLastLoginAt(LocalDateTime.now());
		user.setBirthday(LocalDate.now());
		user.setStatus(3);
		EmbeddedObject1 eo = new EmbeddedObject1();
		eo.setInt1(1);
		eo.setFloat1(2);
		eo.setDouble1(3);
		eo.setString1("4");
		user.setEmbeddedObject1(eo);
		//单条保存 返回key
		Key<User> key = datastore.save(user, new InsertOptions().writeConcern(WriteConcern.UNACKNOWLEDGED));
		System.out.println(key);
	}

	@Test
	public void test_query() {
		Query<User> q = datastore.createQuery(User.class);
		//这里的field 可以是属性名 也可以是数据库字段名
		q.and(
			q.criteria("username").equal("xzc1"),
			q.criteria("username").equal("xzc1")
		);

		//q.or()
		//q.filter("username = ", "xzc1");
		//q.field("a").equal(1).field("b").equal(2) 这样默认是and

		q.order("username");//username asc
		//q.order("username, -birthday");//username asc, birthday desc
		System.out.println(q.asList(
			new FindOptions().skip(0).limit(2)
		));
	}

	@Test
	public void test_query2() {
		//投影username
		User list = datastore.find(User.class)
			.project("username", true)
			.get();//只获取第一个
		System.out.println(list);
	}


	@Test
	public void test_update() {
		UpdateOperations<User> uo = datastore.createUpdateOperations(User.class)
			.set("password", "1")
			//.inc("a",2)
			//.dec("b")
			//.push("a",2)
			//.addToSet()
			//.removeFirst()
			//.removeLast()
			//.removeAll()
			.unset("birthday")
			//.set("a.b", "embedded")
			;
		//datastore.updateFirst()
		UpdateResults ur = datastore.update(datastore.createQuery(User.class).field("username").equal("xzc"), uo);
		System.out.println(ur);
	}

	@Test
	public void set_upsert() {
		//第三个参数 createIfMissing
		//datastore.update(null, null, true);
	}

	@Test
	public void test_delete() {
		//datastore.delete()
	}
}
