package com.xzchaoo.learn.db.springdata.jpa.repository;

import com.xzchaoo.learn.db.springdata.jpa.entity.User;

import java.util.List;

/**
 * 为 UserRepository 单独定制方法
 */
public interface UserRepositoryCustom {
    List<User> findFoo();
}
