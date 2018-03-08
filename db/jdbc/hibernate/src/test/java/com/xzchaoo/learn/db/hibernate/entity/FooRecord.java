package com.xzchaoo.learn.db.hibernate.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author zcxu
 * @date 2018/1/11
 */
@Entity
public class FooRecord {
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, updatable = false)
	private String content;

	@ManyToOne
	private User user;

	//不支持JDK8的时间
	@CreationTimestamp
	private Date createdTime;

	@CreationTimestamp
	private LocalDateTime createdTime2;

	//最好用于表示数据本身的创建/更新时间 而不是业务上的时间
	@UpdateTimestamp
	private LocalDateTime updatedTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
}
