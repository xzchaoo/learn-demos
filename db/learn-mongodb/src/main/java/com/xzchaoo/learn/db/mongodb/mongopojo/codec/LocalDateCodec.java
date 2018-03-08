package com.xzchaoo.learn.db.mongodb.mongopojo.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * created by zcxu at 2017/10/26
 *
 * @author zcxu
 */
public class LocalDateCodec implements Codec<LocalDate> {
    @Override
    public LocalDate decode(BsonReader reader, DecoderContext decoderContext) {
        long mills = reader.readDateTime();
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(mills), ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    public void encode(BsonWriter writer, LocalDate value, EncoderContext encoderContext) {
        writer.writeDateTime(value.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    @Override
    public Class<LocalDate> getEncoderClass() {
        return LocalDate.class;
    }
}
