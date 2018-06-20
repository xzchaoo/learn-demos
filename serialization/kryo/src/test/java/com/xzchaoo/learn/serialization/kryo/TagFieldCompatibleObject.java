package com.xzchaoo.learn.serialization.kryo;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;
import com.esotericsoftware.kryo.serializers.VersionFieldSerializer;

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
public class TagFieldCompatibleObject {
  @TaggedFieldSerializer.Tag(1)
  private int aint;
  @TaggedFieldSerializer.Tag(value = 2)
  private String astring;
//  @TaggedFieldSerializer.Tag(3)w
//  private String foo;
//  @TaggedFieldSerializer.Tag(value = 4)
//  private TagFieldCompatibleNestedObject1 nestedObject1;
}
