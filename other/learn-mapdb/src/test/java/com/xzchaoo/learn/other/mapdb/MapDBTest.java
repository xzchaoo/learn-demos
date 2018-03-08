package com.xzchaoo.learn.other.mapdb;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.mapdb.Atomic;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.IndexTreeList;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerArrayTuple;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Ignore
public class MapDBTest {

	private static final long MB = 1024 * 1024;

	private static void run(String name, Runnable r) {
		Stopwatch sw = Stopwatch.createStarted();
		try {
			r.run();
		} finally {
			long mills = sw.stop().elapsed(TimeUnit.MILLISECONDS);
			System.out.println(String.format("%s 耗时=%d毫秒", name, mills));
		}
	}

	@Test
	public void test_composite() {
		//插入100W个元素 需要18秒
		DB db = DBMaker.tempFileDB().make();
		HTreeMap.KeySet<Object[]> set1 = db.hashSet("set1", new SerializerArrayTuple(Serializer.STRING, Serializer.STRING))
			.counterEnable()
			.createOrOpen();
		List<Object[]> list = new ArrayList<>(1000000);
		for (int i = 0; i < 100_0000; ++i) {
			list.add(new Object[]{"a" + i, "b" + i});
		}
		long begin = System.currentTimeMillis();
		set1.addAll(list);
		long end = System.currentTimeMillis();
		System.out.println((end - begin) / 1000);
		db.close();
	}

	@Test
	public void 内存_set_插入_50W() {
		//耗时4000毫秒左右
		final int size = 500000;
		final int minLength = 4;
		final int maxLength = 10;
		DB db = DBMaker.memoryDB()
			.allocateStartSize(10 * MB)
			.allocateIncrement(10 * MB)
			.make();
		HTreeMap.KeySet<String> set1 = db.hashSet("set1", Serializer.STRING)
			.counterEnable()
			.createOrOpen();
		set1.clear();
		List<String> list = new ArrayList<>(size);
		Random r = new Random();
		for (int i = 0; i < size; ++i) {
			int length = minLength + r.nextInt(maxLength - minLength);
			list.add(RandomStringUtils.random(length, true, true));
		}
		run("内存set,插入50W元素 addAll", new Runnable() {
			@Override
			public void run() {
				set1.addAll(list);
			}
		});
		set1.clear();
		run("内存set,插入50W元素 add", new Runnable() {
			@Override
			public void run() {
				for (String s : list) {
					set1.add(s);
				}
			}
		});
		db.close();
	}

	@Test
	public void ceshi() {
		DB db = DBMaker.memoryDB().closeOnJvmShutdown().make();
		IndexTreeList<Integer> list = db.indexTreeList("a", Serializer.INTEGER).createOrOpen();
		for (int i = 0; i < 50_0000; ++i) {
			list.add(i);
		}
		db.close();
	}

	@Test
	public void 内存_set_转成java里的List() {
		//耗时2000毫秒
		final int size = 500000;
		final int minLength = 20;
		final int maxLength = 80;
		DB db = DBMaker.memoryDB()
			.allocateStartSize(10 * MB)
			.allocateIncrement(10 * MB)
			.make();
		HTreeMap.KeySet<String> set1 = db.hashSet("set1", Serializer.STRING)
			.counterEnable()
			.createOrOpen();
		Random r = new Random();
		List<String> list = new ArrayList<>(size);
		for (int i = 0; i < size; ++i) {
			int length = minLength + r.nextInt(maxLength - minLength);
			String value = RandomStringUtils.random(length, true, true);
			list.add(value);
			set1.add(value);
		}
		System.out.println("开始执行...");
		run("内存set 转成java里的list", new Runnable() {
			@Override
			public void run() {
				new ArrayList<>(set1);
			}
		});
		db.close();
	}

	@Test
	public void 内存_set_contains5次() {
		//耗时9000毫秒左右 怎么会这样... hashset貌似有问题 超级慢 用treeSet 得了...
		final int size = 50_000;
		DB db = DBMaker.memoryDB().closeOnJvmShutdown().make();
		NavigableSet<Integer> set0 = db.treeSet("set1", Serializer.INTEGER).counterEnable().createOrOpen();
		Set<Integer> set1 = set0;
		Random r = new Random();
		for (int i = 0; i < size; ++i) {
			int value = r.nextInt();
			set1.add(value);
		}
		long begin = System.currentTimeMillis();
		set1.contains(1);
		long end = System.currentTimeMillis();
		System.out.println(end - begin);//0ms
		//2000 ms
		db.close();
	}

	@Test
	public void 内存_map_插入50W() {
		//耗时5000毫秒左右
		final int size = 50_0000;
		final int minLength = 20;
		final int maxLength = 80;
		DB db = DBMaker.memoryDB().closeOnJvmShutdown().make();
		HTreeMap<Integer, String> map = db.hashMap("myinfo", Serializer.INTEGER, Serializer.STRING).counterEnable().createOrOpen();
		List<String> values = new ArrayList<>(size);
		Random r = new Random();
		for (int i = 0; i < size; ++i) {
			int length = minLength + r.nextInt(maxLength - minLength);
			String value = RandomStringUtils.random(length, true, true);
			values.add(value);
		}
		run("内存hashmap, 插入50W", new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < size; ++i) {
					map.put(i, values.get(i));
				}
			}
		});
		db.close();
	}

	@Test
	public void 内存_map_查询50W() {
		//耗时800毫秒左右
		final int size = 50_0000;
		final int minLength = 20;
		final int maxLength = 80;
		DB db = DBMaker.memoryDB().closeOnJvmShutdown().make();
		HTreeMap<Integer, String> map = db.hashMap("myinfo", Serializer.INTEGER, Serializer.STRING)
			.counterEnable()
			.createOrOpen();
		Map<Integer, String> map0 = Maps.newHashMapWithExpectedSize(size);
		Random r = new Random();
		for (int i = 0; i < size; ++i) {
			int key = r.nextInt();
			int length = minLength + r.nextInt(maxLength - minLength);
			String value = RandomStringUtils.random(length, true, true);
			map0.put(key, value);
			map.put(key, value);
		}
		run("内存hashmap, 查询key50W次", new Runnable() {
			@Override
			public void run() {
				for (int key : map0.keySet()) {
					map.containsKey(key);
				}
			}
		});
		run("内存hashmap, getByKey50W次", new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < size; ++i) {
					map.get(r.nextInt());
				}
			}
		});
		db.close();
	}

	@Test
	public void 内存_set_contains5次_2() {
		//耗时9000毫秒左右 怎么会这样...
		final int size = 50000;
		final int minLength = 20;
		final int maxLength = 80;
		HTreeMap.KeySet<String> set1 = DBMaker.memoryShardedHashSet(8)
			.serializer(Serializer.STRING)
			.counterEnable()
			.createOrOpen();
		Random r = new Random();
		List<String> list = new ArrayList<>(size);
		for (int i = 0; i < size; ++i) {
			int length = minLength + r.nextInt(maxLength - minLength);
			String value = RandomStringUtils.random(length, true, true);
			list.add(value);
			set1.add(value);
		}
		System.out.println("开始执行...");
		run("内存_set_contains5次_2", new Runnable() {
			@Override
			public void run() {
				set1.contains("asdf");
				set1.contains("asdf");
				set1.contains("asdf");
				set1.contains("asdf");
				set1.contains("asdf");
			}
		});
	}

	@Test
	public void test_3() {
		//插入100W个元素 带size需要7秒 不带size需要7秒
		DB db = DBMaker.memoryDB()
//			.fileMmapEnable()
//			.fileMmapEnableIfSupported()
//			.fileMmapPreclearDisable()
			.allocateStartSize(10 * 1024 * 1024)
			.allocateIncrement(10 * 1024 * 1024)
			.closeOnJvmShutdown()
			.make();
		HTreeMap.KeySet<String> set1 = db.hashSet("set1", Serializer.STRING)
			.counterEnable()
			.createOrOpen();
		List<String> list = new ArrayList<>(1000000);
		for (int i = 0; i < 100_0000; ++i) {
			list.add("a" + i);
		}
		long begin = System.currentTimeMillis();
		set1.addAll(list);
		long end = System.currentTimeMillis();
		System.out.println((end - begin) / 1000);
		db.close();
	}


	@Test
	public void test_size() {
		DB db = DBMaker.fileDB(new File("db/test_size.db"))
			.fileMmapEnable()
			.allocateStartSize(100 * 1024 * 1024)//初始化100MB
			.allocateIncrement(10 * 1024 * 1024L)//增长单位是 10MB
			.closeOnJvmShutdown()
			.make();
		try {
			Atomic.Integer ai = db.atomicInteger("ai1")
				.createOrOpen();
			System.out.println(ai.incrementAndGet());
			Atomic.String as = db.atomicString("as", "1").createOrOpen();
			System.out.println(as.get());
			as.set(Integer.toString(Integer.parseInt(as.get()) + 1));
		} finally {
			db.close();
		}
	}

	@Test
	public void test_transaction_1() {
		DB db = DBMaker.fileDB(new File("db/test_transaction_1.db"))
			//.fileMmapEnableIfSupported()
			.checksumHeaderBypass()
			.closeOnJvmShutdown()
			.make();
		try {
			IndexTreeList<Integer> list = db.indexTreeList("list1", Serializer.INTEGER)
				.createOrOpen();
			list.clear();
			for (int i = 0; i < 10000; ++i) {
				System.out.println(i);
				list.add(i);
			}
			System.out.println(list);
		} finally {
			System.out.println("finally");
			db.close();
		}
	}

	@Test
	public void test_set() {
		DB db = DBMaker.fileDB(new File("db/test_set.db"))
			.fileMmapEnableIfSupported()
			.closeOnJvmShutdown()
			.make();
		try {
			HTreeMap.KeySet<Integer> set1 = db.hashSet("set1", Serializer.INTEGER)
				.counterEnable()
				.createOrOpen();
			for (int i = 0; i < 10000; ++i) {
				set1.addAll(Collections.emptyList());
			}
		} finally {
			db.close();
		}
	}

	@Test
	public void test_map() {
		DB db = DBMaker.fileDB(new File("db/test_map.db"))
			.fileMmapEnableIfSupported()
			.closeOnJvmShutdown()
			.make();
		try {
			//用layout方法调整map的构建 已获得更大的容量 详情见文档
			//线程安全
			HTreeMap<Integer, JSONObject> myinfo = db.hashMap("myinfo", Serializer.INTEGER, new JSONObjectSerializer(JSONObject.class))
				.counterEnable()
				.createOrOpen();
			System.out.println(myinfo.size());
			for (int i = 0; i < 10000; ++i) {
				JSONObject value = myinfo.get(i);
				System.out.println(value);
			}
			myinfo.close();
		} finally {
			db.close();
		}
	}

	@Test
	public void test_3kw_file_set() {
		DB db = DBMaker.fileDB("db/test_3kw_file_set.db")
			.fileChannelEnable()
			.fileMmapEnable()
			.fileMmapEnableIfSupported()
			.allocateStartSize(10 * MB)
			.allocateIncrement(10 * MB)
			.make();
		NavigableSet<Integer> badMids = db.treeSet("badMids", Serializer.INTEGER).createOrOpen();
		run("插入2000W数据", new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 20000000; ++i) {
					badMids.add(10000000 + i);
				}
			}
		});
		run("查询N次", new Runnable() {
			@Override
			public void run() {
				System.out.println(badMids.contains(1));
				System.out.println(badMids.contains(2));
			}
		});
		db.close();
	}

	@Test
	public void test() {
		DB db = DBMaker.fileDB(new File("D:\\idea_workspace\\bilibili-accounts-2\\db\\myinfo.db"))
			.fileChannelEnable()
			.fileMmapEnable()
			.fileMmapEnableIfSupported()
			.allocateStartSize(10 * MB)
			.allocateIncrement(10 * MB)
			.readOnly()
			.make();
		HTreeMap<Integer, String> newPwdMap = db.hashMap("newPwd", Serializer.INTEGER, Serializer.STRING).open();
		System.out.println(new HashMap<>(newPwdMap));
		db.close();
	}

	@Test
	public void test_expire() throws InterruptedException {
		ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);
		DB db = DBMaker.memoryDB().make();
		HTreeMap<String, String> map = db.hashMap("stat", Serializer.STRING, Serializer.STRING)
			.expireExecutor(ses)
			.expireAfterGet(1000)
			.expireAfterCreate(3000)
			.expireExecutorPeriod(10000)//1秒检查一次
			.createOrOpen();
		map.put("a", "aa");
		for (int i = 0; i < 10; ++i) {
			System.out.println(map.get("a"));
			Thread.sleep(3000);
		}
	}
}
