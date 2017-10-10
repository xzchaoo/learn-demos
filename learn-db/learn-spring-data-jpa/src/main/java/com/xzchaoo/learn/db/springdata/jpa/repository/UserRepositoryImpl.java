package com.xzchaoo.learn.db.springdata.jpa.repository;

import com.querydsl.jpa.JPQLQueryFactory;
import com.xzchaoo.learn.db.springdata.jpa.entity.QUser;
import com.xzchaoo.learn.db.springdata.jpa.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {
    /**
     * 这里无法注入 userRepository
     */
//    @Autowired
//    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JPQLQueryFactory jqf;

    @Transactional(readOnly = true)
    @Override
    public List<User> findFoo() {
        //自己实现这个方法
        return jqf.select(QUser.user).from(QUser.user).fetch();
    }
}
