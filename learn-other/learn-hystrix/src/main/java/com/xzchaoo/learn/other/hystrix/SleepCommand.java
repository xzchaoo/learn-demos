package com.xzchaoo.learn.other.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * created by zcxu at 2017/10/25
 *
 * @author zcxu
 */
public class SleepCommand extends HystrixCommand<String> {
    private final int mills;

    public SleepCommand(int mills) {
        super(
            Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("g1"))
                .andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter()
                        //.withFallbackEnabled(true) fallback 是否可用
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                        //.withExecutionTimeoutEnabled(true) 默认实体rue
                        //执行超时会打断线程 - 可配置
                        .withExecutionTimeoutInMilliseconds(1000)//超时时间
                )
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("p1"))
                .andThreadPoolPropertiesDefaults(
                    HystrixThreadPoolProperties.Setter()
                        .withCoreSize(1)
                        .withMaximumSize(2)
                        .withMaxQueueSize(3)
                )
        );
        this.mills = mills;
    }

    @Override
    protected String run() throws Exception {
        Thread.sleep(mills);
        return Integer.toString(mills);
    }
}
