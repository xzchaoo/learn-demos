package com.xzchaoo.learn.db.hibernate.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author zcxu
 * @date 2018/1/11
 */
@Converter
public class GenderConverter implements AttributeConverter<Gender, Character> {
	@Override
	public Character convertToDatabaseColumn(Gender attribute) {
		return attribute == null ? null : attribute.getCode();
	}

	@Override
	public Gender convertToEntityAttribute(Character dbData) {
		return dbData == null ? null : Gender.fromCode(dbData);
	}
}
