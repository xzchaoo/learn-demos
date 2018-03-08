package com.xzchaoo.learn.spring.data.jpa.batch;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BatchAppConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BatchTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TransactionHelper transactionHelper;

    @PersistenceContext
    private EntityManager em;

    private int count = 200;

    @Autowired
    private DataSource dataSource;

    @Test
    public void test3() throws Exception {
        //插入2W条 2秒
        Connection c = dataSource.getConnection();
        long begin = System.currentTimeMillis();
        c.setAutoCommit(false);
        PreparedStatement ps = c.prepareStatement("insert into user2(name) values(?)");
        for (int i = 0; i < count; ++i) {
            ps.setString(1, "xzc" + i);
            ps.addBatch();
        }
        int[] result = ps.executeBatch();
        ps.close();
        c.commit();
        long end = System.currentTimeMillis();
        for (int r : result) {
            System.out.println(r);
        }
        System.out.println(end - begin);
        c.close();
    }

    @Autowired
    private EntityManagerFactory emf;

    @Test
    public void test4() {
        SessionFactory sf = emf.unwrap(SessionFactory.class);
        Session s = sf.openSession();
        Transaction t = s.beginTransaction();
        int batchSize2 = 20;
        long begin = System.currentTimeMillis();
        for (int i = 0; i < count; ++i) {
            User user = new User();
            user.setName("xzc" + i);
            s.save(user);
            if (i % batchSize2 == 0) {
                s.flush();
                s.clear();
            }
        }
        s.flush();
        s.clear();
        t.commit();
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
        s.close();
    }

    @Test
    public void test2() {
        //20000 18907
        //20000 16202
        long begin = System.currentTimeMillis();
        int batchSize2 = 2000;
        transactionHelper.runInTransaction(new Runnable() {
            @Override
            public void run() {
                Session s = em.unwrap(Session.class);
                for (int i = 0; i < count; ++i) {
                    User user = new User();
                    user.setName("xzc" + i);
                    em.persist(user);
                    if (i % batchSize2 == 0) {
                        em.flush();
                        em.clear();
                    }
                }
                em.flush();
                em.clear();
            }
        });
        long end = System.currentTimeMillis();
        System.out.println(count + " " + (end - begin));
    }

    @Test
    public void test1() {
        long begin = System.currentTimeMillis();
        transactionHelper.runInTransaction(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; ++i) {
                    User user = new User();
                    user.setName("xzc" + i);
                    repository.save(user);
                }
            }
        });
        long end = System.currentTimeMillis();
        System.out.println(count + " " + (end - begin));
    }
}
