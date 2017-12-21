package com.xzchaoo.learn.jdk8.jcf.map;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/3/21.
 */
public class MapApp {
    @Test
    public void test() {
        //JDK8 新增了几个方法 用起来非常方便
        Map<String, Object> map = new HashMap<>();
        //如果不存在才放进去 返回旧值/已有的值
        assertNull(map.putIfAbsent("a", 1));
        assertEquals(1, map.putIfAbsent("a", 1));

        Object c = map.computeIfAbsent("b", key -> {
            assertEquals("b", key);
            return "c";
        });
        assertEquals("c", c);

        c = map.computeIfAbsent("b", key -> {
            assertEquals("b", key);
            return "c2";
        });
        assertEquals("c", c);
    }
}
