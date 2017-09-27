package com.xzchaoo.learn.apache.commons.text;

import org.apache.commons.text.RandomStringGenerator;
import org.junit.Test;

public class RandomStringGeneratorTest {
    @Test
    public void test() {
        RandomStringGenerator rsg = new RandomStringGenerator.Builder()
            .withinRange('a', 'z')
            .build();
        System.out.println(rsg.generate(10));
    }
}
