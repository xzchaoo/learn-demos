package com.xzchaoo.learn.rxjava;

import org.junit.Test;

import io.reactivex.Single;
import io.reactivex.subjects.SingleSubject;

/**
 * created by zcxu at 2017/10/31
 *
 * @author zcxu
 */
public class SingleTest {
    @Test
    public void test3() {
        Single.just(1).doOnSuccess(x -> {
            System.out.println("成功1 " + x);
        }).map(x -> 2).doOnSuccess(x -> {
            System.out.println("成功2 " + x);
        }).subscribe();
    }

    @Test
    public void test2() {
        //注意这里doOnSuccess 和 doAfterTerminate doAfterSuccess 等 并不能直接当成回调来用, 注意看它们的注册顺序和调用顺序
        SingleSubject<Integer> ss = SingleSubject.create();
        ss.doAfterSuccess(e -> {//早注册胜利
            System.out.println("成功0");
        }).doAfterSuccess(e -> {
            System.out.println("成功-1");
        }).doOnSuccess(e -> {
            System.out.println("成功1");
        }).doOnSuccess(e -> {
            System.out.println("成功2");
        }).doOnSuccess(e -> {
            System.out.println("成功3");
        }).doOnSuccess(e -> {
            System.out.println("成功4");
        }).doAfterTerminate(() -> {
            System.out.println("成功5");
        }).doAfterTerminate(() -> {//晚注册胜利
            System.out.println("成功6");//后注册的反而先执行
        }).doFinally(() -> {
            System.out.println("i am finally 1");
        }).doFinally(() -> {//晚注册胜利
            System.out.println("i am finally 2");
        }).subscribe();
        ss.onSuccess(1);
    }

    @Test
    public void test1() {
        Single.error(new RuntimeException())
            .flatMap(e -> {
                System.out.println("如果一开始就成功就会走这里的逻辑");
                return Single.just(e);//.onErrorResumeNext();//后来才失败就会走这里
            })//一开始就失败
            .onErrorResumeNext(e -> {
                System.out.println("失败了, 但是我继续");
                return Single.just(3);
            }).subscribe(e -> {
            System.out.println("成功了");
        }, e -> {
            System.out.println("失败了");
        });
        System.out.println(2);
    }
}
