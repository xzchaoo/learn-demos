package com.xzchaoo.learn.archaius2;

import com.netflix.archaius.DefaultPropertyFactory;
import com.netflix.archaius.api.Property;
import com.netflix.archaius.api.PropertyListener;
import com.netflix.archaius.api.exceptions.ConfigException;
import com.netflix.archaius.config.DefaultCompositeConfig;
import com.netflix.archaius.config.DefaultSettableConfig;
import com.netflix.archaius.config.MapConfig;

import org.junit.Test;

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
        pa.addListener(new PropertyListener<Integer>() {
            @Override
            public void onChange(Integer value) {
                //添加监听器的时候会立即触发回调
                System.out.println("new value is " + value);
            }

            @Override
            public void onParseError(Throwable error) {
                error.printStackTrace();
            }
        });
        assertEquals("a", pa.getKey());
        assertEquals(2, pa.get().intValue());
        dsc.setProperty("a", 3);
    }

    @Test
    public void test_DefaultCompositeConfig() throws ConfigException {
        DefaultCompositeConfig dcc = new DefaultCompositeConfig();
        MapConfig mc1 = MapConfig.builder().put("a", "1").build();
        MapConfig mc2 = MapConfig.builder().put("a", "11").put("b", "22").build();
        //靠前的优先级更高
        dcc.addConfig("mc1", mc1);
        dcc.addConfig("mc2", mc2);
        assertEquals("1", dcc.getString("a"));
        assertEquals("22", dcc.getString("b"));

    }
}
