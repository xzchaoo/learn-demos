package com.xzchaoo.learn.serialization.avro;

import com.xzchaoo.learn.serialization.avro.generated.User;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author zcxu
 * @date 2017/12/21
 */
public class AvroTest {
  @Test
  public void test() throws IOException {
    User u = User.newBuilder()
      .setName("haha")
      .setFavoriteColor("RED")
      .setFavoriteNumber(2)
      .build();
    System.out.println(u);

    // Serialize user1, user2 and user3 to disk
    SpecificDatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);
    DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    dataFileWriter.create(u.getSchema(), baos);
    dataFileWriter.append(u);
    dataFileWriter.close();
    System.out.println(baos.size());
    //这种方式会将schema也一同写出去 因此大小比较大


    DatumReader<User> userDatumReader = new SpecificDatumReader<User>(User.class);
    DataFileReader<User> dataFileReader = new DataFileReader<User>(new SeekableByteArrayInput(baos.toByteArray()), userDatumReader);
    User user = null;
    while (dataFileReader.hasNext()) {
// Reuse user object by passing it to next(). This saves us from
// allocating and garbage collecting many objects for files with
// many items.
      user = dataFileReader.next(user);
      System.out.println(user);
    }
  }
}
