package com.xzchaoo.learn.serialization.jackson.mixin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2017/6/15.
 */
//使用下面的annotation可以将类信息序列化出去
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
//@JsonPropertyOrder({指定顺序})
//@JsonAutoDetect()
public abstract class SecurityUserMixin {
	//json支持用特定构造函数来创建对象
	@JsonCreator
	public SecurityUserMixin(@JsonProperty("id") int id, @JsonProperty("name") String name) {
	}

	//@JsonIgnore
	//public abstract String getAge();
}
