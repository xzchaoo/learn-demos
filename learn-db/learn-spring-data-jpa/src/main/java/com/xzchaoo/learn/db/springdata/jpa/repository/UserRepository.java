package com.xzchaoo.learn.db.springdata.jpa.repository;

import com.xzchaoo.learn.db.springdata.jpa.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, QueryDslPredicateExecutor<User> {
}
