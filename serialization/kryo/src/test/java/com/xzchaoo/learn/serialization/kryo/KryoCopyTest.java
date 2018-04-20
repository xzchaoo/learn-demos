package com.xzchaoo.learn.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.pool.KryoPool;

import org.junit.Test;
import org.objenesis.strategy.StdInstantiatorStrategy;

import lombok.val;

/**
 * kryo实现序列化和反序列化的时候 需要知道 如何遍历整个对象图, 因此它也顺便可以用于实现 深浅复制
 * 需要注意的是 kryo 的深浅复制是通过赋值实现的 而不是通过传统的 序列化+ 反序列化 实现的
 * 另外还需要注意的是 kryo 是否支持序列化一些第三方框架的类型, 比如 eclipse-collections joda-time 之类
 *
 * @author zcxu
 * @date 2018/4/16 0016
 */
public class KryoCopyTest {
  private static final KryoPool KRYO_POOL = new KryoPool.Builder(() -> {
    val kryo = new Kryo();
    kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    return kryo;
  }).build();

  @Test
  public void test() {
    // TODO kryo 无法对第三方集合类型做深复制
    // val o1 = new CopyObject();
    // o1.setNames(Sets.immutable.of("a", "b", "c"));
    // val o2 = KRYO_POOL.run(k -> k.copy(o1));
  }
}
