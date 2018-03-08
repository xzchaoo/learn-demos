package com.xzchaoo.learn.db.springdata.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * 自定义的base仓库必须要有这个注解 否则会被当成一个普通的仓库而产生实例化!
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface MyBaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, QueryDslPredicateExecutor<T> {
    void batchSave(Iterable<T> entities);
}
