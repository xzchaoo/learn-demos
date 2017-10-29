package com.xzchaoo.learn.serialization.jackson.fastxmljackson.protobuf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BigDecimalWrapper {
	@JsonProperty(value = "length", index = 1)
	private int length;

	@JsonProperty(value = "raw", index = 2)
	private String raw;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}
}
