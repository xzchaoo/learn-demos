package com.xzchaoo.learn.rxjava.examples.scanvideos;

import org.junit.Test;

import java.util.List;
import java.util.Random;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ScanVideoExample {
    private static Single<String> search(int aid) {
        return Single.create(e -> {
            System.out.println(aid + " 启动一个新线程");
            new Thread(() -> {
                try {
                    int time = new Random().nextInt(2000);
                    System.out.println(aid + " " + time);
                    Thread.sleep(time + 100);
                    e.onSuccess(aid + "-" + time);
                } catch (InterruptedException ex) {
                    e.onError(ex);
                }
            }).start();
        });
    }

    @Test
    public void test() {
        //假设现在要查询 [100,200) 的视频信息
        List<ScanVideoContext> result = Flowable.range(100, 10)
            .map(aid -> {
                ScanVideoContext ctx = new ScanVideoContext();
                ctx.aid = aid;
                return ctx;
            }).flatMapSingle(ctx -> search(ctx.aid).map(response -> {
                ctx.response = response;
                return ctx;
            }), true, 4)//最多4个并发
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .toList()
            .blockingGet();
        System.out.println(result);
    }
}
