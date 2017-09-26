package com.xzchaoo.learn.ehcache2;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.MemoryUnit;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.SearchAttribute;
import net.sf.ehcache.config.Searchable;
import net.sf.ehcache.search.Attribute;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Results;

/**
 * Created by zcxu on 2016/11/11.
 */
public class App {
    public static void main(String[] args) {
        User user = new User();
        user.setId(1);
        user.setUsername("xzc");
        user.setPassword("pass");
        user.setGender(1);

        //可以采用配置文件的方式或者自行配置
        Configuration cfg = new Configuration();
        cfg.diskStore(new DiskStoreConfiguration().path("cache/"));
        cfg.setDefaultCacheConfiguration(
            new CacheConfiguration()
                .eternal(false)
                .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.NONE))
        );
        cfg.setMaxBytesLocalDisk("50G");
        cfg.setMaxBytesLocalHeap("1G");

        //只有放在内存里的数据才能用搜索
        //要求 overflowToDisk = false 才能关闭移除到硬盘的配置
        Searchable searchable = new Searchable();
        searchable.keys(false);
        searchable.values(false);
        searchable.allowDynamicIndexing(false);

        searchable
            .searchAttribute(new SearchAttribute().name("username"))
            .searchAttribute(new SearchAttribute().name("gender"));


        //注册一个名字为a的缓存
        cfg.addCache(new CacheConfiguration()
            .name("a")
            .copyOnRead(true)
            .copyOnWrite(true)
            //.diskPersistent(false)
            //.overflowToDisk(false)//必须得指定 虽然已经被废弃了
            .maxBytesLocalHeap(200, MemoryUnit.MEGABYTES)
            //.maxBytesLocalDisk(10, MemoryUnit.GIGABYTES)
            .eternal(false)
            .diskExpiryThreadIntervalSeconds(120)
            //.persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP))
            .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.NONE))
            .searchable(searchable)
        );

        CacheManager cm = new CacheManager(cfg);
        Cache c = cm.getCache("a");
        c.put(new Element(Integer.toString(user.getId()), user));
        Attribute<Integer> gender = c.getSearchAttribute("gender");
        Query query = c.createQuery()
            //.includeAttribute()
            .includeValues()
            .addCriteria(gender.eq(1)).maxResults(10).end();
        Results results = query.execute();
        System.out.println(results.size());
        System.out.println(results.all().get(0).getValue() == user);
        System.out.println(results.all().get(0).getValue());
        cm.shutdown();

    }
}
