package com.xzchaoo.learn.db.hibernate.entity;

import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.SQLUpdate;
import org.hibernate.annotations.Where;

import java.util.UUID;

import javax.annotation.Generated;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author zcxu
 * @date 2018/1/11
 */
@Entity
@Table(name = "books")
@Where(clause = "isDeleted = 0")
//@SQLUpdate()
public class Book {
	@Id
	@GeneratedValue
	private Long id;

	private String title;

	//uuid可以存成二进制格式 以获得高效 而不是字符串格式
	private UUID uuid;

	@ManyToOne
	@JoinColumn(name = "author_user_id")
	private User author;

	private int isDeleted;

//	@Generated( value = GenerationTime.ALWAYS )
//	@Column(columnDefinition =
//		"AS CONCAT(" +
//			"    COALESCE(firstName, ''), " +
//			"    COALESCE(' ' + middleName1, ''), " +
//			"    COALESCE(' ' + middleName2, ''), " +
//			"    COALESCE(' ' + middleName3, ''), " +
//			"    COALESCE(' ' + middleName4, ''), " +
//			"    COALESCE(' ' + middleName5, ''), " +
//			"    COALESCE(' ' + lastName, '') " +
//			")")
//	private String fullName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
}
