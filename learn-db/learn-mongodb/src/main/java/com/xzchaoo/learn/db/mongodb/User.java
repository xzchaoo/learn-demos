package com.xzchaoo.learn.db.mongodb;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.EntityListeners;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.NotSaved;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 支持生命周期的回调
 */
//@PreLoad
//@PrePersist
//@PreSave
//@PostPersist
//@PostLoad
@EntityListeners({})
/**
 * 被entity标记的是顶级文档
 */
@Entity("users")
//类级别的索引 这里可以定义复合索引
@Indexes({

})
public class User {
    //最终会是文档的_id字段
    /**
     * 这里有没有可能自动实现主键递增呢?
     */
    @Id
    private ObjectId id;

    @Indexed(options = @IndexOptions(unique = true))
    private String username;


    //用于定制属性 否则会使用java bean规范的属性
    @Property("password2")
    private String password;

    @Property
    @Indexed
    private LocalDate birthday;

    @Property
    private LocalDateTime lastLoginAt;

    @Indexed
    @Property
    private int status;

    //@Embedded()
    //EO1已经是一个内嵌类型了 所以这里不用
    private EmbeddedObject1 embeddedObject1;

    @Embedded
    private EmbeddedObject2 eo2;

    @Transient
    private int ignoreMe;

    @NotSaved
    private int notSaveButCanRead;

    //@AlsoLoad("password")
    @Transient
    private String password2;

    //@Reference 可以保存其他对象的引用 DBRef

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public EmbeddedObject1 getEmbeddedObject1() {
        return embeddedObject1;
    }

    public void setEmbeddedObject1(EmbeddedObject1 embeddedObject1) {
        this.embeddedObject1 = embeddedObject1;
    }

    public EmbeddedObject2 getEo2() {
        return eo2;
    }

    public void setEo2(EmbeddedObject2 eo2) {
        this.eo2 = eo2;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", birthday=" + birthday +
            ", lastLoginAt=" + lastLoginAt +
            ", status=" + status +
            ", embeddedObject1=" + embeddedObject1 +
            ", eo2=" + eo2 +
            ", ignoreMe=" + ignoreMe +
            ", notSaveButCanRead=" + notSaveButCanRead +
            ", password2='" + password2 + '\'' +
            '}';
    }
}
