package com.xzchaoo.learn.db.mongodb;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDate;

/**
 * created by zcxu at 2017/10/26
 *
 * @author zcxu
 */
public class UserPojo {
	@BsonId
	private ObjectId id;

	@BsonProperty
	private String name;

	@BsonProperty
	private LocalDate birthday;

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "UserPojo{" +
			"id=" + id +
			", name='" + name + '\'' +
			", birthday=" + birthday +
			'}';
	}
}
