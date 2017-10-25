package com.xzchaoo.learn.db.mongodb;

import com.mongodb.MongoClient;

import org.junit.Before;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.MapperOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseMorphiaTest {
	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
	protected Morphia morphia;
	protected Datastore datastore;

	@Before
	public void before() {
		morphia = new Morphia();

		//映射 这个类 需要有 Entity 标记
		morphia.map(User.class);
		morphia.map(FlightEntity.class);

		//morphia.mapPackage() 也可以用扫包法

		MapperOptions options = new MapperOptions();
		morphia.getMapper().setOptions(options);

		//默认情况下Morphia 不会保存空数组 空map 和 null
//		options.setStoreEmpties(true);
//		options.setStoreNulls(true);

		//datastore 封装了 mongo client 从这以后 morphia 对象就基本没用了 除非你要用相同的配置连到多个不同的数据库
		datastore = morphia.createDatastore(new MongoClient("106.14.13.32", 27777), "test");
		//datastore = morphia.createDatastore(new MongoClient("106.14.175.164", 27777), "test");
		//保证索引都创建了
		datastore.ensureIndexes();

		//datastore.createAggregation(null).skip(1)
	}
}
