package com.xzchaoo.learn.serialization.kryo;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author xzchaoo
 * @date 2018/3/31
 */
@Getter
@Setter
@ToString
public class SearchCriteria {
  @TaggedFieldSerializer.Tag(1)
  private boolean aboolean;
  @TaggedFieldSerializer.Tag(2)
  private byte abyte;
  @TaggedFieldSerializer.Tag(3)
  private short ashort;
  @TaggedFieldSerializer.Tag(4)
  private int aint;
  @TaggedFieldSerializer.Tag(5)
  private float afloat;
  @TaggedFieldSerializer.Tag(6)
  private long along;
  @TaggedFieldSerializer.Tag(7)
  private double adouble;
  @TaggedFieldSerializer.Tag(8)
  private String astring;
  @TaggedFieldSerializer.Tag(9)
  private boolean[] booleans;
  @TaggedFieldSerializer.Tag(10)
  private byte[] bytes;
  @TaggedFieldSerializer.Tag(11)
  private short[] shorts;
  @TaggedFieldSerializer.Tag(12)
  private int[] ints;
  @TaggedFieldSerializer.Tag(13)
  private float[] floats;
  @TaggedFieldSerializer.Tag(14)
  private long[] longs;
  @TaggedFieldSerializer.Tag(15)
  private double[] doubles;
  @TaggedFieldSerializer.Tag(16)
  private String[] strings;
  //private String localDate;
//  @TaggedFieldSerializer.Tag(17)
//  private String test;
}
