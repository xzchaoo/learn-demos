package com.xzchaoo.learn.jdk8.classloader;

/**
 * @author xzchaoo
 * @date 2018/6/3
 */
public class MyClassLoader extends ClassLoader {
  public MyClassLoader(ClassLoader parent) {
    super(parent);
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    // 如果需要改变加载顺序 则重写这个方法
    return super.loadClass(name);
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    // 如果需要改变加载类的来源 则重写这个方法
    // super.defineClass()
    return super.findClass(name);
  }
}
