package com.xzchaoo.learn.apache.commons.io;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import lombok.val;

/**
 * @author xzchaoo
 * @date 2018/6/20
 */
public class FileMonitorTest {
  @Test
  public void test() throws Exception {
    val monitor = new FileAlterationMonitor();
    val o1 = new FileAlterationObserver("C:/dev");
    CountDownLatch cdl = new CountDownLatch(1);
    o1.addListener(new FileAlterationListenerAdaptor() {
      @Override
      public void onFileChange(File file) {
        cdl.countDown();
        System.out.println(file);
      }
    });
    monitor.addObserver(o1);
    monitor.start();
    cdl.await();
  }
}
