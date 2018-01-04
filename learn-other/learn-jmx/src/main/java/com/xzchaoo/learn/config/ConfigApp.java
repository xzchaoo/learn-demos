package com.xzchaoo.learn.config;


import com.netflix.archaius.ConfigProxyFactory;
import com.netflix.archaius.DefaultConfigLoader;
import com.netflix.archaius.DefaultPropertyFactory;
import com.netflix.archaius.api.Property;
import com.netflix.archaius.api.PropertyFactory;
import com.netflix.archaius.api.PropertyListener;
import com.netflix.archaius.api.annotations.DefaultValue;
import com.netflix.archaius.api.config.CompositeConfig;
import com.netflix.archaius.api.config.PollingStrategy;
import com.netflix.archaius.api.exceptions.ConfigException;
import com.netflix.archaius.cascade.ConcatCascadeStrategy;
import com.netflix.archaius.config.DefaultCompositeConfig;
import com.netflix.archaius.config.DefaultSettableConfig;
import com.netflix.archaius.config.MapConfig;
import com.netflix.archaius.config.PollingDynamicConfig;
import com.netflix.archaius.config.polling.FixedPollingStrategy;
import com.netflix.archaius.config.polling.PollingResponse;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/1/8.
 */
public class ConfigApp {
	public interface ApplicationConfig {
		@DefaultValue("0")
		int getA();

		@DefaultValue("0")
		String getB();
	}

	@Test
	public void test1() throws ConfigException, InterruptedException {
		//支持替换 ${foo:default}

		Properties props = new Properties();
		props.setProperty("env", "dev");

		CompositeConfig lib = new DefaultCompositeConfig();
		CompositeConfig app = new DefaultCompositeConfig();

		DefaultSettableConfig dyn = new DefaultSettableConfig();
		//最顶层是动态属性

		PollingStrategy strategy = new FixedPollingStrategy(1, TimeUnit.SECONDS);
		PollingDynamicConfig config = new PollingDynamicConfig(new Callable<PollingResponse>() {
			public PollingResponse call() throws Exception {
				System.out.println("执行");
				return new PollingResponse() {
					@Override
					public Map<String, String> getToAdd() {
						Map<String, String> map = new HashMap<String, String>();
						map.put("application.a", "1234");
						return map;
					}

					@Override
					public Collection<String> getToRemove() {
						return null;
					}

					@Override
					public boolean hasData() {
						return true;
					}
				};
			}
		}, strategy);

		CompositeConfig cfg = DefaultCompositeConfig.builder()
			.withConfig("dyn", dyn)
			.withConfig("com/xzchaoo/learn/config", config)
			.withConfig("lib", lib)
			.withConfig("app", app)
			.withConfig("props", MapConfig.from(props))
			.build();

		DefaultConfigLoader loader = DefaultConfigLoader.builder()
			.withStrLookup(cfg)
			.withDefaultCascadingStrategy(ConcatCascadeStrategy.from("${env}"))
			.build();

		app.replaceConfig("application", loader.newLoader().load("application"));
		Assert.assertEquals("ddd+asdf", cfg.getString("application.d"));
		Assert.assertEquals(1234, cfg.getInteger("application.a").intValue());
		Assert.assertEquals(22, cfg.getInteger("application.b").intValue());
		Assert.assertEquals(3, cfg.getInteger("application.c").intValue());
		//Assert.assertTrue(cfg.getBoolean("application-prod.loaded", false));

		lib.replaceConfig("libA", loader.newLoader().load("libA"));
		lib.replaceConfig("libB", loader.newLoader().load("libB"));
		Assert.assertTrue(cfg.getBoolean("libA.loaded", false));
		Assert.assertTrue(cfg.getBoolean("libA-dev.loaded", false));
		Assert.assertEquals(11, cfg.getInteger("libA.a").intValue());
		Assert.assertEquals(2, cfg.getInteger("libA.b").intValue());

		PropertyFactory factory = DefaultPropertyFactory.from(cfg);
		Property<Integer> aa = factory.getProperty("application.a").asInteger(0);
		System.out.println(aa.get());
		aa.addListener(new PropertyListener<Integer>() {
			public void onChange(Integer value) {
				System.out.println("value=" + value);
			}

			public void onParseError(Throwable error) {
				error.printStackTrace();
			}
		});
		//dyn.setProperty("application.a", 111);


		ConfigProxyFactory proxy = new ConfigProxyFactory(cfg, cfg.getDecoder(), factory);
		ApplicationConfig ac = proxy.newProxy(ApplicationConfig.class, "application");
		System.out.println(ac.getA());
		//dyn.setProperty("application.a", 1111);
		System.out.println(ac.getA());


		Thread.sleep(5000);
		System.out.println(ac.getA());
		//cfg.accept(new PrintStreamVisitor());
	}
}
