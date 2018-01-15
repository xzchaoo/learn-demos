package com.xzchaoo.learn.db.hibernate.versiontest;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * @author zcxu
 * @date 2018/1/12
 */
@Entity
//@DynamicUpdate
@OptimisticLocking(type = OptimisticLockType.VERSION)
//@SelectBeforeUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
//@Cacheable
public class VTUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, updatable = false, unique = true)
	private String username;

	@OptimisticLock(excluded = false)
	private String password;

	@Column(updatable = false)
	private LocalDateTime registerTime;
	private LocalDateTime lastLoginTime;

	@Version
	private long version;

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

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
}
