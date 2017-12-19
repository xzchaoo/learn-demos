package com.xzchaoo.learn.mapstruct;

import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * created by xzchaoo at 2017/11/22
 *
 * @author xzchaoo
 */
@Mapper()
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	//@Mapping(source = "numberOfSeats", target = "seatCount")
	//@Mapping(source = "channels",expression = "")
	@Mapping(target = "e4", dateFormat = "yyyyMMdd")
	User convertUserEntityToUser(UserEntity entity);

	void fillUserEntityToUser(UserEntity entity, @MappingTarget User user);

	default LocalDateTime toLocalDateTime(Date date) {
		if (date == null) {
			return null;
		}
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
//	@ValueMapping(source = "0", target = "SIMPLE")
//	@ValueMapping(source = "1", target = "VIP")
//	@ValueMapping(source = "2", target = "ADMIN")
//	UserType convertIntToUserType(int userType);

	default List<String> channels(String channels) {
		if (StringUtils.isEmpty(channels)) {
			return null;
		}
		return Lists.newArrayList(StringUtils.split(channels, '/'));
	}

	UserEntity copy(UserEntity e);


}
