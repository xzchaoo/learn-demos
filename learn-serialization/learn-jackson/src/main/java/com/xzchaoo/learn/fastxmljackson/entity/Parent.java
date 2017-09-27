package com.xzchaoo.learn.fastxmljackson.entity;

/**
 * Created by xzchaoo on 2016/6/5 0005.
 */

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

//表示这个类型的标识符是 p
@JsonTypeName("p")
//表示这个类已知的子类
@JsonSubTypes({@JsonSubTypes.Type(Child1.class), @JsonSubTypes.Type(Child2.class)})

@JsonTypeInfo(
	use = JsonTypeInfo.Id.NAME,//表示使用类型标识符作为 多态的识别
	include = JsonTypeInfo.As.PROPERTY,//表示将你用于识别的信息放在 属性上
	property = "type"//表示属性的名字叫做type
)
public class Parent {
	private Integer id;
	private String name;

	/*
		@JsonTypeId
		private String type;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}
	*/
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
