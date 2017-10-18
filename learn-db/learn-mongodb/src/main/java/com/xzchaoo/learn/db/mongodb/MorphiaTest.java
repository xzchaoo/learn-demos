package com.xzchaoo.learn.db.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.InsertOptions;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.UpdateOptions;
import org.mongodb.morphia.mapping.MapperOptions;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * mongodb官方出品, 基于 mongo-java-driver
 * TODO 目前根据官方文档, 能写的demo大概就这么多 其他demo得在实战中积累
 */
public class MorphiaTest {

    private Morphia morphia;
    private Datastore datastore;

    @Before
    public void before() {
        morphia = new Morphia();

        //映射 这个类 需要有 Entity 标记
        morphia.map(User.class);

        //morphia.mapPackage() 也可以用扫包法

        MapperOptions options = new MapperOptions();
        morphia.getMapper().setOptions(options);

        //默认情况下Morphia 不会保存空数组 空map 和 null
//		options.setStoreEmpties(true);
//		options.setStoreNulls(true);

        //datastore 封装了 mongo client 从这以后 morphia 对象就基本没用了 除非你要用相同的配置连到多个不同的数据库
        datastore = morphia.createDatastore(new MongoClient("106.14.13.32", 27777), "test");
        //保证索引都创建了
        datastore.ensureIndexes();
    }

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
            .set("password", "1")
            //常见的mongodb的更新操作都有
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
}
