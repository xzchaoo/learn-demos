package com.xzchaoo.learn.other.mapdb;

import com.alibaba.fastjson.JSON;

import org.jetbrains.annotations.NotNull;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerString;

import java.io.IOException;

/**
 * Created by Administrator on 2017/3/31.
 */
public class JSONObjectSerializer<T> implements Serializer<T> {
	private static final SerializerString s = (SerializerString) Serializer.STRING;
	private final Class<T> clazz;

	public JSONObjectSerializer(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	public void serialize(@NotNull DataOutput2 out, @NotNull T value) throws IOException {
		s.serialize(out, JSON.toJSONString(value));
	}

	@Override
	public T deserialize(@NotNull DataInput2 input, int available) throws IOException {
		String value = s.deserialize(input, available);
		return JSON.parseObject(value,clazz);
	}
}
