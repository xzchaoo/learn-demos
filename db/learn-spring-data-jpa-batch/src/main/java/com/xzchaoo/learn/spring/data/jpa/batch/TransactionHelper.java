package com.xzchaoo.learn.spring.data.jpa.batch;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionHelper {
    @Transactional
    public void runInTransaction(Runnable r) {
        r.run();
    }
}
