package com.xzchaoo.learn.ehcache2;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * 这里介绍了用手动配置的办法, 使用ehcache.xml配置的时候其实概念都是一样的 换成xml形式而已
 */
public class Ehcache2Test {
	@Test
	public void test_basic() throws Exception {
		//getInstance会返回一个单例
		CacheManager cm = CacheManager.getInstance();
		System.out.println(Arrays.asList(cm.getCacheNames()));
	}

	@Test
	public void test_basic2() throws Exception {
		//newInstance每次都会new一个
		CacheManager cm = CacheManager.newInstance();
		System.out.println(Arrays.asList(cm.getCacheNames()));
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
				.name("a")//缓存的名字叫做a
				.maxEntriesLocalHeap(4)//在内存里的最大的元素个数
				.eternal(false)//元素是否是永恒的 如果是永恒的 那么会忽略所有的过期时间 需要自己手动控制元素的清除
				.timeToLiveSeconds(4)//一个元素自从加入缓存 4秒后肯定过期
				.timeToIdleSeconds(2)//一个元素一旦空闲超过2秒 就过期, 如果有访问或修改 就会重新计时
				//完全内存级别的缓存
				.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LRU)
				.persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.NONE))
				//.maxEntriesInCache(4) 这个只能用于集群模式 当然你设置了最多就不生效 不至于会异常
				.copyOnRead(false)//如果要启动 copyOnXxx系列的方法 则value必须要是可以序列化的
				.copyOnWrite(false)//建议放入缓存的对象都是不可修改的 可以通过设计模式来保证
		);

		//手动构造一个cm 也可以使用静态构造函数来构建 此时会读取 classpath:ehcache.xml 的配置
		CacheManager cm = new CacheManager(cfg);

		//cm在创建之后也可以再通过下面的方法添加缓存
		//cm.addCache();

		Cache ca = cm.getCache("a");
		CacheObject co = new CacheObject();
		co.a = 1;

		assertNull(ca.get("k1"));
		//加入5个元素 由于上面配置了最大个数是4 因此第一个元素会被去除
		for (int i = 0; i < 5; ++i) {
			//一个element封装了key/value信息
			ca.put(new Element("k" + i, co));
			Thread.sleep(10);
		}
		//这里偷偷修改了a的值为2 由于没有启动 copyOnWrite 因此 下面的输出都是2, 都受影响了!
		co.a = 2;
		//可以get很多指标
		//System.out.println(ca.getStatistics());

		//可以看到第一个元素已经被移除了
		//一段时间之后 全部数据都会因为过期而被删掉
		for (int j = 0; j < 5; ++j) {
			for (int i = 2; i < 5; ++i) {
				//这里可以拿到element 如果为null表示不存在 否则Element里封装了一些信息 必须命中率 最后一次访问时间等
				System.out.println(ca.get("k" + i));
			}
			if (j == 2) {
				//k1因为idle太久而过期
				System.out.println("k1=" + ca.get("k1"));
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
