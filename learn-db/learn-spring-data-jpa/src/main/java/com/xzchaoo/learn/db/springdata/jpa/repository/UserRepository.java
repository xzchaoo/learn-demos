package com.xzchaoo.learn.db.springdata.jpa.repository;

import com.xzchaoo.learn.db.springdata.jpa.entity.User;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 还没测试过底层的批量保存是 batch 还是 single
 */
@Repository
public interface UserRepository extends MyBaseRepository<User, Integer>, UserRepositoryCustom {
    //基本的定制方法
    User findOneByName(@Param("name") String name);

    //自己制定JPQL
    @Query("select u from User as u where u.name = :name")
    User findOneByNamexxx(@Param("name") String name);

    //update 和 delete 需要指定 modifying
    @Modifying
    @Query("delete from User")
    int deleteAllXxx();
}
