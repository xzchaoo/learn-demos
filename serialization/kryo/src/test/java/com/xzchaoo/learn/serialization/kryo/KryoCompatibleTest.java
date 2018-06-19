package com.xzchaoo.learn.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import lombok.val;

/**
 * @author xzchaoo
 * @date 2018/6/15
 */
public class KryoCompatibleTest {
  private static final KryoPool FIELD_POOL = new KryoPool.Builder(Kryo::new).build();
  private static final KryoPool TAG_POOL = new KryoPool.Builder(() -> {
    Kryo kryo = new Kryo();
    kryo.setDefaultSerializer(TaggedFieldSerializer.class);
    kryo.getTaggedFieldSerializerConfig().setSkipUnknownTags(true);
    return kryo;
  }).build();

  @Test
  public void test_field_write() {
//    1. 加字段 ×
//    2. 删字段 ×
//    3. 字段改名 √, 类型不变OK的
//    4. 改类型 ?, 部分兼容, int和long可能是互相兼容的(只要实际范围兼容)
//    1. int是变长编码, 长度是1~5
//    2. long是变长编码, 长度是1~? 只要落在1~5的范围内都是兼容的, 但有可能出现溢出
    val obj = new FieldCompatibleObject();
    //obj.setAint(Integer.MAX_VALUE*70862L);
    obj.setAstring2("bbb");
    val baos = new ByteArrayOutputStream();
    FIELD_POOL.run(k -> {
      try ( Output output = new Output(baos) ) {
        k.writeObject(output, obj);
      }
      return null;
    });
    System.out.println(encode(baos.toByteArray()));
  }

  @Test
  public void test_field_read() {
    FieldCompatibleObject obj = FIELD_POOL.run(k -> {
      try ( Input output = new Input(decode("AeSs9//fmUUBYmLi")) ) {
        return k.readObject(output, FieldCompatibleObject.class);
      }
    });
    System.out.println(obj);
  }

  @Test
  public void test_tag_write() {
    val obj = new TagFieldCompatibleObject();
    obj.setAint(1);
    obj.setAstring("Asdf");
    //obj.setFoo("foo");
    val no1 = new TagFieldCompatibleNestedObject1();
    no1.setA(2);
    no1.setB("3");
    //obj.setNestedObject1(no1);
    val baos = new ByteArrayOutputStream();
    TAG_POOL.run(k -> {
      try ( Output output = new Output(baos) ) {
        k.writeObject(output, obj);
      }
      return null;
    });
    System.out.println(encode(baos.toByteArray()));
  }

  @Test
  public void test_tag_read() {
    val obj = TAG_POOL.run(k -> {
      try ( Input input = new Input(decode("AQMBAgIBQXNk5gMBZm/v")) ) {
        return k.readObject(input, TagFieldCompatibleObject.class);
      }
    });
    System.out.println(obj);
  }


  private static String encode(byte[] data) {
    return Base64.getEncoder().encodeToString(data);
  }

  private static byte[] decode(String str) {
    return Base64.getDecoder().decode(str);
  }
}
