package com.xzchaoo.learn.serialization.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;
import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * TODO kryo其实还是很有复杂度的 建议没完全掌握之前别其轻易使用
 * kryo实例不是线程安全的, 需要自己用pool或ThreadLocal的方式管理起来
 * 注意 DefaultSerializer 的选择
 * 默认的是 FieldSerializer 它序列化的时候不会带Schema信息的, 也不会有字段index, 完全是按照每个字段的顺序进行序列化的, 因此所有字段不能被跳过(null会用特殊值来标记) 也不能添加 删除 字段, 修改字段定义也不行, 会造成不兼容
 * 由于 FieldSerializer 太苛刻了, 容易导致不兼容, 因此更常用的是 TaggedFieldSerializer 序列化的时候它会含有字段index, 因此要求所有DTO上都用@Tag打上标记, 支持 添加 删除 字段
 * 另外一个是 VersionFieldSerializer 没怎么研究过 有空看下
 *
 * @author xzchaoo
 * @date 2018/3/31
 */
public class KryoTest {

  private KryoPool pool;

  @Before
  public void before() {
    // 注意该pool只会扩容 不会缩容(如果你总是记得release的话)
    pool = new KryoPool.Builder(() -> {
      //每次都要在这里进行初始化 因为这是一个新的实例
      Kryo kryo = new Kryo();
      kryo.setDefaultSerializer(TaggedFieldSerializer.class);
      kryo.getTaggedFieldSerializerConfig().setSkipUnknownTags(true);
      kryo.register(SearchRequest.class);
      return kryo;
    }).build();
  }

  @Test
  public void testDeserislize() {
    byte[] data = Base64.getDecoder().decode("AQIBAQBjb20ueHpjaGFvby5sZWFybi5zZXJpYWxpemF0aW9uLmtyeW8uUmVxdWVzdEhlYWRl8gEAAgEBY29tLnh6Y2hhb28ubGVhcm4uc2VyaWFsaXphdGlvbi5rcnlvLlNlYXJjaENyaXRlcmnhAREBAQICAwADBAgFQKAAAAYMB0AcAAAAAAAACAGCOAkBAwEACgALAAwADQAOAA8AEAARAA==");
    Kryo kryo = pool.borrow();
    SearchRequest req2 = kryo.readObject(new Input(data), SearchRequest.class);
    System.out.println(req2);
    pool.release(kryo);
  }

  @Test
  public void testSerialize() {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Output output = new Output(baos);
    SearchRequest req1 = new SearchRequest();
    req1.setRequestHeader(new RequestHeader());
    SearchCriteria sc = new SearchCriteria();
    sc.setAboolean(true);
    sc.setAbyte((byte) 2);
    sc.setAshort((short) 3);
    sc.setAint(4);
    sc.setAfloat(5);
    sc.setAlong(6);
    sc.setAdouble(7);
    sc.setAstring("8");
    sc.setBooleans(new boolean[]{true, false});
    req1.setSearchCriteria(sc);

    pool.run(kryo -> {
      kryo.writeObject(output, req1);
      return null;
    });
    output.close();
    byte[] data = baos.toByteArray();
    System.out.println(data.length);
    System.out.println(Base64.getEncoder().encodeToString(data));
  }
}
