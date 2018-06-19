package com.xzchaoo.learn.serialization.kryo;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xzchaoo
 * @date 2018/6/15
 */
@Getter
@Setter
@ToString
public class TagFieldCompatibleNestedObject1 {
  @TaggedFieldSerializer.Tag(1)
  private int a;
  @TaggedFieldSerializer.Tag(2)
  private String b;
}
