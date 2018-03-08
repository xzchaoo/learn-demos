package com.xzchaoo.learn.db.hibernate.entity;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author zcxu
 * @date 2018/1/11
 */
@Entity
@Table(name = "users")
//@NamedEntityGraphs({
//	@NamedEntityGraph(
//		name = "foo",
//		attributeNodes = {@NamedAttributeNode("projects")}
//	)
//})
public class User {
	@Id
	@GeneratedValue
	private Long id;
	@Column(updatable = false)
	private String username;
	private String password;

	//@BatchSize(size = 5)
	@JoinTable(name = "users_phones_foo", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "phone_id"))
	@OneToMany(cascade = {CascadeType.ALL})
	private List<Phone> phones;

	private Address address;

	@Convert(converter = GenderConverter.class)
	private Gender gender;

	//需要启动字节码增强 否则lazy是无效的
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private Blob avatar;

	@Column(updatable = false)
	private LocalDateTime registerTime;

	private LocalDateTime lastLoginTime;

	@ElementCollection
	private List<String> nicknames;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public LocalDateTime getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(LocalDateTime registerTime) {
		this.registerTime = registerTime;
	}

	public LocalDateTime getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(LocalDateTime lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Blob getAvatar() {
		return avatar;
	}

	public void setAvatar(Blob avatar) {
		this.avatar = avatar;
	}
}
