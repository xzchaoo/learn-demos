package com.xzchaoo.learn.springboot.aop;

import org.springframework.stereotype.Service;

/**
 * @author zcxu
 */
@Service
public class XxxServiceImpl implements XxxService {
    @Timed(scenario = "flight-lowpricebot-mongodb-write", action = "mongodbWrite")
    @Override
    public int save(int in) {
        return in + 1;
    }
}
