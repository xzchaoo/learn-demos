package com.xzchaoo.learn.utils.time;

import java.util.concurrent.Callable;

/**
 * created by zcxu at 2017/10/26
 *
 * @author zcxu
 */
public class TimeRecorder {

    public static void execute(Runnable runnable, LongCallback callback) {
        if (callback != null) {
            long begin = System.currentTimeMillis();
            runnable.run();
            long end = System.currentTimeMillis();
            callback.callback(end - begin);
        } else {
            runnable.run();
        }
    }

    public static <T> T execute(Callable<T> callable, LongCallback callback) throws Exception {
        T ret = null;
        if (callback != null) {
            long begin = System.currentTimeMillis();
            ret = callable.call();
            long end = System.currentTimeMillis();
            callback.callback(end - begin);
        } else {
            ret = callable.call();
        }
        return ret;
    }

}
