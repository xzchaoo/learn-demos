package com.xzchaoo.learn.jdk8.process;

import org.junit.Test;

import java.io.IOException;
import java.util.Scanner;

/**
 * 进程控制
 *
 * @author zcxu
 * @date 2018/2/1
 */
public class ProcessTest {
  @Test
  public void test() throws IOException, InterruptedException {
    ProcessBuilder pb = new ProcessBuilder("tasklist");
    Process p = pb.start();
    Scanner scanner = new Scanner(p.getInputStream(), "gb2312");
    while (scanner.hasNextLine()) {
      System.out.println(scanner.nextLine());
    }
    p.waitFor();
    System.out.println("进程结束");
  }
}
