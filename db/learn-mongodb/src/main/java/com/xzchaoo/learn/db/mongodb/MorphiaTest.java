package com.xzchaoo.learn.db.mongodb;

import com.mongodb.WriteConcern;

import org.bson.Document;
import org.junit.Test;
import org.mongodb.morphia.InsertOptions;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.UpdateOptions;
import org.mongodb.morphia.aggregation.Accumulator;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import static org.mongodb.morphia.aggregation.Group.grouping;
import static org.mongodb.morphia.aggregation.Projection.projection;

/**
 * mongodb官方出品, 基于 mongo-java-driver
 * TODO 目前根据官方文档, 能写的demo大概就这么多 其他demo得在实战中积累
 */
public class MorphiaTest extends BaseMorphiaTest {

	/**
	 * 展示如何保存数据
	 */
	@Test
	public void test_save() {
		User user = new User();
		user.setUsername("xzc2");
		user.setPassword("xxx333");
		user.setLastLoginAt(LocalDateTime.now());
		user.setBirthday(LocalDate.now());
		user.setStatus(3);
		EmbeddedObject1 eo = new EmbeddedObject1();
		eo.setInt1(1);
		eo.setFloat1(2);
		eo.setDouble1(3);
		eo.setString1("4");
		user.setEmbeddedObject1(eo);
		EmbeddedObject2 eo2 = new EmbeddedObject2();
		eo2.setInt1(11);
		user.setEo2(eo2);
		//单条保存 返回key
		//注意这里是 UNACKNOWLEDGED 它不会等待mongodb的回应! 所以如果因为违反unique而插入失败 也不会提示错误的
		//因为_id是在本地生成的
		Key<User> key = datastore.save(user, new InsertOptions().writeConcern(WriteConcern.UNACKNOWLEDGED));
		System.out.println(key);
	}

	@Test
	public void test_query() {
		//创建一个查询
		Query<User> q = datastore.createQuery(User.class);

		//构造查询条件
		//这里的field 可以是属性名 也可以是数据库字段名
		//q.or()
		q.and(
			q.criteria("username").equal("xzc2")
		);

		//filter语法见 http://mongodb.github.io/morphia/1.3/guides/querying/
		//filter过滤应该和上面是类似的
		//q.filter("username = ", "xzc1");

		//q.field("a").equal(1).field("b").equal(2) 这样默认是and

		//q.project() 投影
		//排序
		q.order("username");//username asc
		//q.order("username, -birthday");//username asc, birthday desc

		//文本匹配
		//q.search()

		//limit
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
	public void test_query3() {
		List<User> list = datastore.find(User.class)
			.field("username").contains("xzc")
			.field("password").exists()
			.asList();
		System.out.println(list);
	}

	@Test
	public void test_update() {
		UpdateOperations<User> uo = datastore.createUpdateOperations(User.class)
			.set("password", "111")
			//常见的mongodb的更新操作都有
			//.inc("a",2)
			//.dec("b")
			//.push("a",2)
			//.addToSet()
			//.removeFirst()
			//.removeLast()
			//.removeAll()
			//.unset("birthday")
			//.set("a.b", "embedded")
			;
		//datastore.updateFirst()
		//TODO 这里有一个陷阱, 因为我们自己指定了 options 所以默认的options就不会用了
		UpdateOptions options = new UpdateOptions();
		options.multi(true);//默认的options的multi是为true的 因此我们也要记得加上
		UpdateResults ur = datastore.update(datastore.createQuery(User.class).field("username").contains("abc"), uo, options);
		System.out.println(ur);
	}

	@Test
	public void test_update2() {
		UpdateOperations<User> incSalary = datastore.createUpdateOperations(User.class)
			.inc("salary", 1)
			.set("foo", "foo")//设置foo属性
			.unset("bar");//删除bar属性
		Query<User> maleUsers = datastore.createQuery(User.class).field("gender").equal("MALE");
		datastore.update(maleUsers, incSalary, new UpdateOptions());
	}

	@Test
	public void test_querydsl() {
		//querydsl的体验应该是一致的 但是似乎没有支持最新版本的morphia 现在有一些运行时问题
		//并且jar包之间的编译错误 是不会体现在本差鞥徐的编译错误上的
		QUser user = QUser.user;
		MyMongodbQuery<User> q = new MyMongodbQuery<>(morphia, datastore, user);
		q.where(user.username.contains("xzc"));
		System.out.println(q.fetch());
	}

	public static class A {
		private String id;
		private int count;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		@Override
		public String toString() {
			return "A{" +
				"id='" + id + '\'' +
				", count=" + count +
				'}';
		}
	}

	@Test
	public void test_insert1() {
		for (int i = 0; i < 10; ++i) {
			User user = new User();
			user.setUsername("abc" + i);
			user.setPassword("123");
			datastore.save(user);
		}
	}

	@Test
	public void test_aggregate() {
		System.out.println(datastore.createQuery(User.class).asList());
		Iterator<?> iter = datastore.createAggregation(User.class)
			.match(datastore.createQuery(User.class).field("username").contains("abc"))
			.project(
				projection("username", "password2"), projection("password2", "username"),
				projection("status")
				//add("$status","$status")
			)
			.group("username",
				grouping("count", Accumulator.accumulator("$sum", 1)),
				grouping("statusSum", Accumulator.accumulator("$sum", "status"))
			)
			//.skip(0).limit(1)
			//.out(User.class);
			.aggregate(Document.class);
		while (iter.hasNext()) {
			System.out.println("here");
			System.out.println(iter.next());
		}
	}
}
