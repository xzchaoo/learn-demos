package com.xzchaoo.learn.db.springdata.jpa.repository;

import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QueryDslJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

import javax.persistence.EntityManager;

@Transactional(readOnly = true)
public class MyBaseRepositoryImpl<T, ID extends Serializable> extends QueryDslJpaRepository<T, ID> implements MyBaseRepository<T, ID> {
    private EntityManager em;
    private final JPAQueryFactory jqf;

    public MyBaseRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.em = entityManager;
        jqf = new JPAQueryFactory(HQLTemplates.DEFAULT, entityManager);
    }

    @Override
    public void batchSave(Iterable<T> entities) {
        int count = 0;
        for (T t : entities) {
            em.persist(t);
            if (++count == 100) {
                count = 0;
                em.flush();
                em.clear();
            }
        }
        if (count > 0) {
            em.flush();
            em.clear();
        }
    }

//    public MyBaseRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager, EntityPathResolver resolver) {
//        super(entityInformation, entityManager, resolver);
//    }
}
