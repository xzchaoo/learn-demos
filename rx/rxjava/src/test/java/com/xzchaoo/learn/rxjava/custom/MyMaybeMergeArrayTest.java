package com.xzchaoo.learn.rxjava.custom;

import com.xzchaoo.learn.rxjava.custom.maybe.MyMaybeMergeArray;

import org.junit.Test;

import io.reactivex.Maybe;
import io.reactivex.MaybeSource;

/**
 * @author xzchaoo
 * @date 2018/5/13
 */
public class MyMaybeMergeArrayTest {
  @SuppressWarnings("unchecked")
  @Test
  public void test() {
    MyMaybeMergeArray<Integer> a = new MyMaybeMergeArray<>(new MaybeSource[]{
      Maybe.just(1),
      Maybe.error(new RuntimeException())
    });
    System.out.println(a.toList().blockingGet());
  }
}
