package com.xzchaoo.learn.jdk8.stream;

import org.junit.Test;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class StreamTest {
    @Test
    public void test1() {
        //流式语法
        List<User> list = IntStream.range(0, 1000)
            .map(x -> x * x)
            .map(x -> (int) Math.sqrt(x))
            .boxed().map(x -> {
                User user = new User();
                user.setId(x);
                user.setName("x" + x);
                return user;
            }).filter(u -> u.getId() % 3 == 0)
            .sorted(Comparator.comparingInt(User::getId).reversed())
            .collect(Collectors.toList());
        assertEquals(334, list.size());
        assertEquals(999, list.get(0).getId());
    }
}
