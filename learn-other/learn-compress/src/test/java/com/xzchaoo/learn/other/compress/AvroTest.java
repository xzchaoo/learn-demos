package com.xzchaoo.learn.other.compress;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import example.avro.User;

public class AvroTest {
	@Test
	public void test() throws IOException {
		User u = User.newBuilder().setId(1).setName("xzc").setLastLoginAt(System.currentTimeMillis()).build();
		DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
		DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		dataFileWriter.create(u.getSchema(), baos);
		dataFileWriter.append(u);
		dataFileWriter.close();
		System.out.println(baos.size());
	}
}
