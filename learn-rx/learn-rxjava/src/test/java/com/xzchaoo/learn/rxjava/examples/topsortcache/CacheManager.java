//package com.xzchaoo.learn.rxjava.examples.topsortcache;
//
//import org.eclipse.collections.api.multimap.set.MutableSetMultimap;
//import org.eclipse.collections.api.set.MutableSet;
//import org.eclipse.collections.impl.factory.Multimaps;
//
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//
//import io.reactivex.Completable;
//import io.reactivex.Flowable;
//
///**
// * @author zcxu
// * @date 2018/1/18
// */
//public class CacheManager {
//  private List<Cache> caches;
//
//  public CacheManager(List<Cache> caches) {
//    this.caches = caches;
//  }
//
//  public void initRefreshAll() {
//    MutableSetMultimap<Cache, Cache> dependencies = Multimaps.mutable.set.empty();
//    BarCache bar = new BarCache();
//    UserCache user = new UserCache();
//    FooCache foo = new FooCache(bar, user);
//
//    dependencies.put(foo, bar);
//    dependencies.put(foo, user);
//
//    ConcurrentHashMap<Cache, Completable> map = new ConcurrentHashMap<>();
//    Flowable.fromIterable(caches).flatMapCompletable(cache -> {
//      doRefreshCached(map, dependencies, cache);
//      MutableSet<Cache> deps = dependencies.get(cache);
//      if (deps != null) {
//        Completable c = Completable.complete();
//        for (Cache dc : deps) {
//          c = c.andThen(doRefresh(map, dc));
//        }
//        return c.andThen(doRefresh(map, cache));
//      }
//    });
//  }
//
//  private Completable doRefreshCached(ConcurrentHashMap<Cache, Completable> map, MutableSetMultimap<Cache, Cache> dependencies, Cache cache) {
//    return map.computeIfAbsent(cache, ignore -> {
//      Completable c = Completable.complete();
//      MutableSet<Cache> deps = dependencies.get(cache);
//      if (deps != null) {
//        for (Cache dep : deps) {
//        }
//      }
//    });
//  }
//}
