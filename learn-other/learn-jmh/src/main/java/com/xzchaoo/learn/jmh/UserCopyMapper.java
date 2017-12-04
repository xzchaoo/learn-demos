package com.xzchaoo.learn.jmh;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * created by zcxu at 2017/12/4
 *
 * @author zcxu
 */
@Mapper
public interface UserCopyMapper {
	UserCopyMapper INSTANCE = Mappers.getMapper(UserCopyMapper.class);

	@Mapping(target="list1",expression = "java(u.getList1())")
	UserForJson shadowCopy(UserForJson u);
}
