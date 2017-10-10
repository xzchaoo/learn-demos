package com.xzchaoo.learn.jdk8.jcf.list;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListTest {
    @Test
    public void test() {
        List<String> list = IntStream.range(0, 100)
            .mapToObj(Integer::toString)
            .filter(x -> x.length() == 1)
            .sorted(Comparator.reverseOrder())
            .collect(Collectors.toList());
        //默认的实现是ArrayList 通过源代码可以看出
        assertTrue(list instanceof ArrayList);
        assertEquals(10, list.size());
        assertEquals("9", list.get(0));

        //这个方法挺好的 内部用了比较好的方式来实现非常低代价的删除!
        list.removeIf(x -> x.compareTo("4") < 0);

        //list还新增了sort方法 原本的 Collections.sort(list) 方法实际上会调用 list.sort()
        //list.sort();

        //这个foreach的语法也挺舒服的 缺点是不兼容JDK7 当要兼容JDK7的时候需要大量修改
        list.forEach(System.out::println);

        assertEquals(6, list.size());
    }
}
