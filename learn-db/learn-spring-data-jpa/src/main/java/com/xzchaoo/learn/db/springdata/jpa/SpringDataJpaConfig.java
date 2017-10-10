package com.xzchaoo.learn.db.springdata.jpa;

import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class SpringDataJpaConfig {
    @PersistenceContext()
    private EntityManager entityManager;

    @Bean
    public JPQLQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(HQLTemplates.DEFAULT, entityManager);
    }
}
