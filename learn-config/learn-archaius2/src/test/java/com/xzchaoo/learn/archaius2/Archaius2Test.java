package com.xzchaoo.learn.archaius2;

import com.netflix.archaius.ConfigProxyFactory;
import com.netflix.archaius.DefaultConfigLoader;
import com.netflix.archaius.DefaultPropertyFactory;
import com.netflix.archaius.api.Config;
import com.netflix.archaius.api.Property;
import com.netflix.archaius.api.PropertyFactory;
import com.netflix.archaius.api.PropertyListener;
import com.netflix.archaius.api.annotations.Configuration;
import com.netflix.archaius.api.annotations.DefaultValue;
import com.netflix.archaius.api.annotations.PropertyName;
import com.netflix.archaius.api.config.CompositeConfig;
import com.netflix.archaius.api.config.PollingStrategy;
import com.netflix.archaius.api.exceptions.ConfigException;
import com.netflix.archaius.cascade.ConcatCascadeStrategy;
import com.netflix.archaius.config.DefaultCompositeConfig;
import com.netflix.archaius.config.DefaultConfigListener;
import com.netflix.archaius.config.DefaultSettableConfig;
import com.netflix.archaius.config.EnvironmentConfig;
import com.netflix.archaius.config.MapConfig;
import com.netflix.archaius.config.PollingDynamicConfig;
import com.netflix.archaius.config.SystemConfig;
import com.netflix.archaius.config.polling.FixedPollingStrategy;
import com.netflix.archaius.config.polling.PollingResponse;
import com.netflix.archaius.visitor.PrintStreamVisitor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class Archaius2Test {
    //常见的辅助类有 EmptyConfig
    //SystemConfig 读取系统属性
    //EnvironmentConfig 读取环境变量
    //PollingDynamicConfig 基于轮询策略更新的配置

    @Test
    public void test_basic_MapConfig() {
        MapConfig mc = MapConfig.builder().put("a", "1").build();
        assertEquals(1, mc.getInteger("a").intValue());
    }

    @Test
    public void test_basic_SettableConfig() {
        DefaultSettableConfig dsc = new DefaultSettableConfig();
        dsc.setProperty("a", 1);
        assertEquals(1, dsc.getInteger("a").intValue());
        dsc.setProperty("a", 2);
        assertEquals(2, dsc.getInteger("a").intValue());

        DefaultPropertyFactory dpf = DefaultPropertyFactory.from(dsc);
        Property<Integer> pa = dpf.getProperty("a").asInteger(1);
        dsc.addListener(new DefaultConfigListener() {
            @Override
            public void onConfigUpdated(Config config) {
                System.out.println("onConfigUpdated");
            }
        });
        //添加监听器的时候会立即触发回调
        pa.addListener(new PropertyListener<Integer>() {
            @Override
            public void onChange(Integer value) {
                System.out.println("onChange new value is " + value);
            }

            @Override
            public void onParseError(Throwable error) {
                error.printStackTrace();
            }
        });

        //当config改变的 时候会回调这里
        dsc.addListener(new DefaultConfigListener() {
            @Override
            public void onConfigAdded(Config config) {
                super.onConfigAdded(config);
            }

            @Override
            public void onConfigUpdated(Config config) {
                System.out.println("onConfigUpdated " + config.getInteger("a"));
            }
        });
        assertEquals("a", pa.getKey());
        assertEquals(2, pa.get().intValue());
        dsc.setProperty("a", 3);
        dsc.setProperty("a", 4);
        dsc.setProperty("a", 5);
    }

    @Test
    public void test_DefaultCompositeConfig() throws ConfigException {
        DefaultCompositeConfig dcc = new DefaultCompositeConfig();
        DefaultSettableConfig dsc = new DefaultSettableConfig();
        MapConfig mc1 = MapConfig.builder().put("a", "1").build();
        MapConfig mc2 = MapConfig.builder().put("a", "11").put("b", "22").build();
        //靠前的优先级更高
        dcc.addConfig("dsc", dsc);
        dcc.addConfig("mc1", mc1);
        dcc.addConfig("mc2", mc2);
        assertEquals("1", dcc.getString("a"));
        assertEquals("22", dcc.getString("b"));
        dcc.addListener(new DefaultConfigListener() {
            @Override
            public void onConfigUpdated(Config config) {
                //这里拿到的是dcc
                System.out.println(config.hashCode());
                System.out.println("onConfigUpdated");
            }
        });
        System.out.println(dcc.hashCode());
        System.out.println(dsc.hashCode());
        dsc.setProperty("aaa", 3);
    }


    /**
     * 构建层叠配置
     *
     * @throws ConfigException
     */
    @Test
    public void testCompositeConfig() throws ConfigException {
        //在实际中 可能会在数据库里提供配置 这部分的配置优先级更高

        MapConfig p1 = MapConfig.builder()
            .put("a", "1")
            .put("c", "3")
            .build();

        MapConfig p2 = MapConfig.builder()
            .put("a", "11")
            .put("b", "22")
            .build();

        //p1等级高
        CompositeConfig cfg = DefaultCompositeConfig.builder()
            .withConfig("p1", p1)
            .withConfig("p2", p2)
            .build();

        assertEquals("1", cfg.getString("a"));
        assertEquals("22", cfg.getString("b"));
        assertEquals("3", cfg.getString("c"));
    }

    /**
     * 获得配置里的所有key, 感觉不是很有用 除非你需要批量获取满足特定格式的配置
     */
    @Test
    public void testKeys() {
        MapConfig p1 = MapConfig.builder()
            .put("a", "1")
            .put("b", "1")
            .build();
        Iterator<String> ki = p1.getKeys();
        List<String> keys = new ArrayList<>();
        ki.forEachRemaining(keys::add);
        assertEquals(2, keys.size());
        Collections.sort(keys);
        assertEquals("a", keys.get(0));
        assertEquals("b", keys.get(1));
    }

    @Test
    public void testPlaceholder() {
        //占位符 变量不需要先定义后使用 只要不会形成环就行了
        MapConfig p1 = MapConfig.builder()
            .put("a", "${c}")
            .put("b", "${a}")
            .put("c", "1")
            .put("d", "${e:1}")//可以配置默认值
            .build();
        assertEquals("1", p1.getString("a"));
        assertEquals("1", p1.getString("b"));
        assertEquals("1", p1.getString("d"));
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoElement() {
        MapConfig p1 = MapConfig.builder()
            .put("a", "1")
            .build();
        //默认情况下 如果元素不存在就会抛出异常
        //通常不建议在代码代码里使用 p1.getString("key", "defaultValue") 这样的重载版本
        //因为需要硬编码 默认值, 如果多个地方用到了 还要保证多个地方的默认值是一样的(通常是需要这样的)
        //建议为所有的配置都提供一份值 保存在文件里
        assertEquals("1", p1.getString("b"));
    }

    @Test
    public void testEnv() throws ConfigException {

        //复合配置
        CompositeConfig app = new DefaultCompositeConfig();
        //app可以将多个配置串在一起 越靠前的配置优先级越高

        //DefaultConfigLoader 的作用是用于加载一个配置文件及其变种
        DefaultConfigLoader loader = DefaultConfigLoader.builder()
            .withDefaultCascadingStrategy(ConcatCascadeStrategy.from("dev", "bak"))
            .build();
        //applicationConfig的内容是由 application-dev-bak.properties 和 application-dev.properties + application.properties 组成的
        //名字越长的优先级越高
        CompositeConfig applicationConfig = loader.newLoader().load("application");
        app.replaceConfig("application", applicationConfig);

        assertEquals("1", app.getString("a"));
        assertEquals("22", app.getString("b"));
        assertEquals("33", app.getString("c"));
    }

    @Test
    public void testPollingDynamicConfig() throws InterruptedException {
        //通过轮训的方式更新配置
        PollingStrategy strategy = new FixedPollingStrategy(100, TimeUnit.MILLISECONDS);
        AtomicInteger ai = new AtomicInteger(0);
        PollingDynamicConfig db = new PollingDynamicConfig(new Callable<PollingResponse>() {
            public PollingResponse call() throws Exception {
                Map<String, String> map = new HashMap<String, String>();
                map.put("a", Integer.toString(ai.incrementAndGet()));
                return PollingResponse.forSnapshot(map);
            }
        }, strategy);
        assertEquals("1", db.getString("a"));
        Thread.sleep(150);
        assertEquals("2", db.getString("a"));
    }

    @Configuration(immutable = true)
    public interface C1 {
        int getA();

        double getB();

        String getC();

        @DefaultValue("ddd")
        String getD();

        @PropertyName(name = "ee")
        Integer getE();
    }

    @Test
    public void test_customType() {
        MapConfig c = MapConfig.builder()
            .put("a", "1 a")
            .build();
        //有 静态的 valueOf 方法 或 单个字符串的构造函数 都可以 进行解析!
        CustomType ct = c.get(CustomType.class, "a");
        System.out.println(ct);
    }

    @Test
    public void testProxyFactory() {
        MapConfig c = MapConfig.builder()
            .put("e.a", 1)
            .put("e.b", 22.2)
            .put("e.c", "333")
            .put("e.ee", "333")
            .build();
        PropertyFactory factory = DefaultPropertyFactory.from(c);
        //通过这个方式你可以避免在其他地方输入属性的名字
        Property<Integer> property = factory.getProperty("e.a").asInteger(0);
        property.addListener(new PropertyListener<Integer>() {
            @Override
            public void onChange(Integer value) {
                System.out.println("值该变了");
            }

            @Override
            public void onParseError(Throwable error) {

            }
        });
        assertEquals(1, property.get().intValue());
    }

    @Test
    public void testConfigProxyFactory() {
        //DefaultSettableConfig 具备更新能力的配置
        //可以用于和任意的框架整合

        DefaultSettableConfig c = new DefaultSettableConfig();
        MapConfig c2 = MapConfig.builder()
            .put("e.a", 1)
            .put("e.b", 22.2)
            .put("e.c", "333")
            .put("e.ee", "333")
            .build();
        c.setProperties(c2);
        c.setProperty("e.a", 2);
        PropertyFactory factory = DefaultPropertyFactory.from(c);
        ConfigProxyFactory proxy = new ConfigProxyFactory(c, c.getDecoder(), factory);
        //可以将配置 绑定到一个接口上
        C1 c1 = proxy.newProxy(C1.class, "e");
        assertEquals(2, c1.getA());
        assertEquals(22.2, c1.getB(), 0.1);
        assertEquals("333", c1.getC());
        assertEquals("ddd", c1.getD());
        assertEquals(333, c1.getE().intValue());
        c.setProperty("e.a", 3);
        assertEquals(2, c1.getA());
    }

    @Test
    public void main() throws ConfigException {
        System.setProperty("spring.profiles.active", "prod,dev");

        DefaultSettableConfig settable = new DefaultSettableConfig();
        CompositeConfig app = new DefaultCompositeConfig();

        PollingStrategy strategy = new FixedPollingStrategy(1, TimeUnit.MINUTES);
        PollingDynamicConfig db = new PollingDynamicConfig(new Callable<PollingResponse>() {
            public PollingResponse call() throws Exception {
                System.out.println("执行");
                Map<String, String> map = new HashMap<String, String>();
                map.put("application.a", "1234");
                return PollingResponse.forSnapshot(map);
            }
        }, strategy);

        CompositeConfig cfg = DefaultCompositeConfig.builder()
            .withConfig("settable", settable)
            .withConfig("db", db)
            .withConfig("app", app)
            .withConfig("env", EnvironmentConfig.INSTANCE)
            .withConfig("system", SystemConfig.INSTANCE)
            .build();

        DefaultConfigLoader loader = DefaultConfigLoader.builder()
            .withStrLookup(cfg)
            //在左边优先级最高
            .withDefaultCascadingStrategy(ConcatCascadeStrategy.from(System.getProperty("spring.profiles.active").split(",")))
            .build();

        app.replaceConfig("application", loader.newLoader().load("application"));

        app.accept(new PrintStreamVisitor());

        System.out.println(cfg.getString("application.a"));
        System.out.println(cfg.getString("a"));
        System.out.println(cfg.getString("b"));
        System.out.println(cfg.getString("c"));

    }
}
