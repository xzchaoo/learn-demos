package com.xzchaoo.learn.db.mongodb.mongopojo.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * created by zcxu at 2017/10/26
 *
 * @author zcxu
 */
public class LocalDateTimeCodec implements Codec<LocalDateTime> {
    @Override
    public LocalDateTime decode(BsonReader reader, DecoderContext decoderContext) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(reader.readDateTime()), ZoneId.systemDefault());
    }

    @Override
    public void encode(BsonWriter writer, LocalDateTime value, EncoderContext encoderContext) {
        writer.writeDateTime(value.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    @Override
    public Class<LocalDateTime> getEncoderClass() {
        return LocalDateTime.class;
    }
}
