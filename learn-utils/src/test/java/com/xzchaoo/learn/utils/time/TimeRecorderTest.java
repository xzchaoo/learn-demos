package com.xzchaoo.learn.utils.time;

import org.junit.Test;

/**
 * created by zcxu at 2017/10/26
 *
 * @author zcxu
 */
public class TimeRecorderTest {
    @Test
    public void test1() {
        TimeRecorder.execute(() -> {
            System.out.println("开始睡觉");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("结束睡觉");
        }, mills -> {
            System.out.println("耗时=" + mills);
        });
    }
}
