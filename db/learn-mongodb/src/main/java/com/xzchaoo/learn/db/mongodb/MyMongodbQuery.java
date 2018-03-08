package com.xzchaoo.learn.db.mongodb;

import com.google.common.base.Function;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.querydsl.core.types.EntityPath;
import com.querydsl.mongodb.AbstractMongodbQuery;
import com.querydsl.mongodb.morphia.MorphiaSerializer;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.cache.DefaultEntityCache;

/**
 * TODO querydsl似乎好久没更新了 并没有支持新版的Morphia 因此需要重新实现以下这个类 不知道用起来会不会有冲突 小心为好
 *
 * @param <K>
 */
public class MyMongodbQuery<K> extends AbstractMongodbQuery<K, MyMongodbQuery<K>> {

    private final Datastore datastore;
    private final DefaultEntityCache cache;

    public MyMongodbQuery(Morphia morphia, Datastore datastore, EntityPath<K> entityPath) {
        this(morphia, datastore, new DefaultEntityCache(), entityPath);
    }

    public MyMongodbQuery(Morphia morphia, Datastore datastore, DefaultEntityCache cache, EntityPath<K> entityPath) {
        this(morphia, datastore, cache, (Class<K>) entityPath.getType());
    }

    public MyMongodbQuery(Morphia morphia, Datastore datastore, DefaultEntityCache cache, Class<K> entityType) {
        super(datastore.getCollection(entityType), new Function<DBObject, K>() {
            @Override
            public K apply(DBObject dbObject) {
                return morphia.fromDBObject(datastore, entityType, dbObject, cache);
            }
        }, new MorphiaSerializer(morphia));
        this.datastore = datastore;
        this.cache = cache;
    }

    @Override
    protected DBCollection getCollection(Class<?> type) {
        return null;
    }
}
