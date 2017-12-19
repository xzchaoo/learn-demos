package com.xzchaoo.learn.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * @author zcxu
 * @date 2017/12/19
 */
@Mapper
public interface FooMapper {
	FooDto convert(FooEntity fe);
}
