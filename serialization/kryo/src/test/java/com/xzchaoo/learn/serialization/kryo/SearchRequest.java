package com.xzchaoo.learn.serialization.kryo;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;

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
public class SearchRequest {
  @TaggedFieldSerializer.Tag(1)
  private RequestHeader requestHeader;
  @TaggedFieldSerializer.Tag(2)
  private SearchCriteria searchCriteria;
}
