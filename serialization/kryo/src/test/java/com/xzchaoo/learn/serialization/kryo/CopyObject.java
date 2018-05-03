package com.xzchaoo.learn.serialization.kryo;

import org.eclipse.collections.api.set.ImmutableSet;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zcxu
 * @date 2018/4/16 0016
 */
@Getter
@Setter
public class CopyObject {
  private ImmutableSet<String> names;
}
