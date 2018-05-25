package com.xzchaoo.learn.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 看到一些代码, 它们缓存了对某个字符串的操作结果
 * 比如我要取一个 url 的 host部分, 它就建立一个cache, 缓存 url->host的映射, 这样效率真的能提高吗?
 * 千万要注意数量 不要撑爆内存了
 * 答案是看情况, 缓存还是缓存, 它的本质并没有变化. 只要你的操作耗时足够多, 那么缓存就可以看出效果, 比如下面的 test1 test2
 * 但是如果你实时计算的代码比 访问缓存的代价要小的话, 那么此时性能是反而下降的, 比如下面的 test3 test4
 *
 * @author xzchaoo
 * @date 2018/5/25
 */
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(1)
@BenchmarkMode(Mode.Throughput)
@State(Scope.Benchmark)
public class StringOperationCachePerformanceTest {
  private int n = 100_0000;
  private List<String> urls;

  @Setup
  public void setup() {
    urls = new ArrayList<>(n);
    Random random = new Random();
    for (int i = 0; i < n; ++i) {
      int x = random.nextInt(10000);
      String url = "http://www.example.com/" + x;
      urls.add(url);
    }
  }

  @Benchmark
  public void test_1(Blackhole b) {
    for (int i = 0; i < n; ++i) {
      String url = urls.get(i);
      URI uri = URI.create(url);
      String host = uri.getHost();
      b.consume(host);
    }
  }

  @Benchmark
  public void test_2(Blackhole b) {
    ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    for (int i = 0; i < n; ++i) {
      String url = urls.get(i);
      String host = cache.computeIfAbsent(url, url2 -> URI.create(url2).getHost());
      b.consume(host);
    }
  }

  @Benchmark
  public void test_3(Blackhole b) {
    for (int i = 0; i < n; ++i) {
      String url = urls.get(i);
      // String host = String.format("%s.%s", url, "haha");
      String host = url + ".haha";
      b.consume(host);
    }
  }

  @Benchmark
  public void test_4(Blackhole b) {
    ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

    for (int i = 0; i < n; ++i) {
      String url = urls.get(i);
      String host = cache.computeIfAbsent(url, url2 -> url2 + ".haha");
      b.consume(host);
    }
  }
}
