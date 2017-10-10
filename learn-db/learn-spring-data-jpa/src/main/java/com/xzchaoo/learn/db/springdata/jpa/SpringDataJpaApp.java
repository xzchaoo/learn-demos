package com.xzchaoo.learn.db.springdata.jpa;

import com.querydsl.jpa.JPQLQueryFactory;
import com.xzchaoo.learn.db.springdata.jpa.entity.QUser;
import com.xzchaoo.learn.db.springdata.jpa.entity.User;
import com.xzchaoo.learn.db.springdata.jpa.repository.MyBaseRepositoryImpl;
import com.xzchaoo.learn.db.springdata.jpa.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = MyBaseRepositoryImpl.class)
public class SpringDataJpaApp implements ApplicationRunner {
    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaApp.class, args);
    }

    /**
     * jqf强无敌
     */
    @Autowired
    private JPQLQueryFactory jqf;

    @Autowired
    private UserRepository userRepository;

    private static final QUser user = QUser.user;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        User u = new User();
        u.setName("xzc");
        //userRepository.save(u);
        userRepository.batchSave(Arrays.asList(u));

        //利用 spring-data-jpa 对 querydsl 的支持
        System.out.println(userRepository.findAll(
            user.name.eq("xzc")
        ));

        //直接用jqf
        System.out.println(jqf.from(user).select(user).fetch());

        System.out.println(userRepository.findFoo());
    }
}