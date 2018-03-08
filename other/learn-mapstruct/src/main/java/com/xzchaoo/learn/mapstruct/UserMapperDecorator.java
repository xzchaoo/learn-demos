package com.xzchaoo.learn.mapstruct;

/**
 * created by xzchaoo at 2017/11/22
 *
 * @author xzchaoo
 */
public abstract class UserMapperDecorator implements UserMapper {
	private final UserMapper mapper;

	public UserMapperDecorator(UserMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public User convertUserEntityToUser(UserEntity entity) {
		User u = mapper.convertUserEntityToUser(entity);
		if (u != null && entity != null) {
			u.setType(convertToUserType(entity.getUserType()));
		}
		return u;
	}

	private UserType convertToUserType(int userType) {
		switch (userType) {
			case 0:
				return UserType.SIMPLE;
			case 1:
				return UserType.VIP;
			case 2:
				return UserType.ADMIN;
			default:
				throw new IllegalArgumentException();
		}
	}
}
