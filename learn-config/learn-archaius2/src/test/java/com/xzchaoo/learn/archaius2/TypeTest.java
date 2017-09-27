package com.xzchaoo.learn.archaius2;

import com.netflix.archaius.DefaultPropertyFactory;
import com.netflix.archaius.api.Config;
import com.netflix.archaius.api.ConfigListener;
import com.netflix.archaius.api.PropertyFactory;
import com.netflix.archaius.api.PropertyListener;
import com.netflix.archaius.config.DefaultSettableConfig;
import com.netflix.archaius.config.MapConfig;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TypeTest {
	@Test
	public void test() throws InterruptedException {
		MapConfig mc = MapConfig.builder()
			.put("a", "1,2,3,4")
			.build();
		DefaultSettableConfig c = new DefaultSettableConfig();
		c.setProperties(mc);

		//所有的基本数据类型的配置都是OK的
		//archaius 内部不具备缓存的能力 因此每次都需要 将 String 拆成List
		List<Integer> list = c.getList("a", Integer.class);
		System.out.println(list);

		ConfigWrapper cw = new ConfigWrapper();
		PropertyFactory pf = DefaultPropertyFactory.from(c);
		//addListener 之后会马上触发一次
		c.addListener(new ConfigListener() {
			@Override
			public void onConfigAdded(Config config) {
				System.out.println("onConfigAdded");
			}

			@Override
			public void onConfigRemoved(Config config) {
				System.out.println("onConfigRemoved");
			}

			@Override
			public void onConfigUpdated(Config config) {
				System.out.println("onConfigUpdated");
			}

			@Override
			public void onError(Throwable error, Config config) {
				System.out.println("onError");
			}
		});
		pf.getProperty("a").asString("").addListener(new PropertyListener<String>() {
			@Override
			public void onChange(String value) {
				cw.nums = Stream.of(value.split(","))
					.map(Integer::parseInt)
					.collect(Collectors.toList());
			}

			@Override
			public void onParseError(Throwable error) {
				error.printStackTrace();
			}
		});
		System.out.println(cw.nums);
		Thread.sleep(1000);
		c.setProperty("a", "233");
		Thread.sleep(1000);
	}
}
