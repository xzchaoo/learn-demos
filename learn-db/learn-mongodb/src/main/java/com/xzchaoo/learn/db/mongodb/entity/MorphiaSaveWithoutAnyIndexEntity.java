package com.xzchaoo.learn.db.mongodb.entity;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 * created by zcxu at 2017/10/25
 *
 * @author zcxu
 */
@Entity(noClassnameStored = true)
public class MorphiaSaveWithoutAnyIndexEntity {
    @Id
    private ObjectId id;
    private String value;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
