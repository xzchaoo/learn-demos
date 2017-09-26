package com.xzchaoo.learn.ehcache2;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.PersistenceConfiguration;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertNull;

public class Ehcache2Test {
    @Test
    public void test_basic() throws Exception {
        CacheManager c = CacheManager.getInstance();
        System.out.println(Arrays.asList(c.getCacheNames()));
    }

    @Test
    public void test_basic2() throws Exception {
        CacheManager c = CacheManager.newInstance();
        System.out.println(Arrays.asList(c.getCacheNames()));
    }

    @Test
    public void test1() throws InterruptedException {
        Configuration cfg = new Configuration();
        cfg.name("xxx-cache-manager");//每个CM还可以有一个名字 通过newInstance创建的话会保证名字一样的CM只有一个

        //注册一个名字为a的缓存
        //最多保存4个对象, 超过之后应该是LRU了
        //每个对象最多存存活4秒 最多空闲2秒 所有数据只保存在内存里 不进行深复制
        cfg.addCache(
            new CacheConfiguration()
                .name("a")
                .maxEntriesLocalHeap(4)
                .eternal(false)
                .timeToLiveSeconds(4)
                .timeToIdleSeconds(2)
                //完全内存级别的缓存
                .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.NONE))
                //.maxEntriesInCache(4) 这个只能用于集群模式
                .copyOnRead(false)//如果要启动 copyOnXxx系列的方法 则value必须要是可以序列化的
                .copyOnWrite(false)//建议放入缓存的对象都是不可修改的 可以通过设计模式来保证
        );

        //手动构造一个cm 也可以使用静态构造函数来构建 此时会读取 classpath:ehcache.xml 的配置
        CacheManager cm = new CacheManager(cfg);

        //手动添加一个cache 似乎没有意义?
        //cm.addCache();

        Cache ca = cm.getCache("a");
        CacheObject co = new CacheObject();
        co.a = 1;

        assertNull(ca.get("k1"));
        for (int i = 0; i < 5; ++i) {
            //一个element封装了key/value信息
            ca.put(new Element("k" + i, co));
            Thread.sleep(10);
        }
        co.a = 2;
        System.out.println(ca.getStatistics());
        //可以看到第一个元素已经被移除了
        for (int j = 0; j < 5; ++j) {
            for (int i = 0; i < 5; ++i) {
                //这里可以拿到element 如果为null表示不存在 否则Element里封装了一些信息 必须命中率 最后一次访问时间等
                System.out.println(ca.get("k" + i));
            }
            Thread.sleep(1000);
        }
        //ca.remove() 删除
        //ca.removeAll(); 清空
        //ca.getSize()
        //不用了之后关闭
        cm.shutdown();
    }
}
