package com.xzchaoo.learn.db.mongodb;

import org.bson.types.ObjectId;
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
@Entity("users")
@Indexes({

})
public class User {
	@Id
	private ObjectId id;

	@Indexed(options = @IndexOptions(unique = true))
	private String username;

	//用于定制属性 否则会使用java属性
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

	@Transient
	private int ignoreMe;

	@NotSaved
	private int notSaveButCanRead;

	//@AlsoLoad("password")
	@Transient
	private String password2;

	//@Reference 可以保存其他对象的引用

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
			", ignoreMe=" + ignoreMe +
			", notSaveButCanRead=" + notSaveButCanRead +
			", password2='" + password2 + '\'' +
			'}';
	}
}
