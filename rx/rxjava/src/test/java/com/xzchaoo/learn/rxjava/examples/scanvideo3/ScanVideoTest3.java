package com.xzchaoo.learn.rxjava.examples.scanvideo3;

import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

/**
 * TODO 当流程复杂到一定程度时, 很难用一张图去表达, 可能需要每个步骤画一张RX图好理解一些
 *
 * @author zcxu
 * @date 2018/3/5 0005
 */
public class ScanVideoTest3 {
  private static class HttpResponse {
  }

  private static class VideoInfo {
    private boolean exists;
  }

  private static class VideoWrapper {
    public int id;
    public VideoInfo videoInfo;
  }

  /**
   * 查询缓存, 缓存支持异步
   *
   * @param id
   * @return
   */
  private ListenableFuture<VideoInfo> rawSearchCache(int id) {
    return null;
  }

  /**
   * 第一个视频请求
   *
   * @param id
   * @return
   */
  private ListenableFuture<HttpResponse> rawHTTPSearch1(int id) {
    return null;
  }

  /**
   * 第二个视频请求
   *
   * @param id
   * @return
   */
  private ListenableFuture<HttpResponse> rawHTTPSearch2(int id) {
    return null;
  }

  private void batchSaveToDB(List<VideoWrapper> list) {

  }

  private VideoInfo merge(HttpResponse r1, HttpResponse r2) {
    boolean success = r1 != null && r2 != null;
    VideoInfo videoInfo = new VideoInfo();
    videoInfo.exists = success;
    return videoInfo;
  }

  private boolean hasResult(VideoInfo videoInfo) {
    return videoInfo.exists;
  }

  private void root() {
    Set<Integer> ids = Sets.newHashSet();
    Set<Integer> successIds = Sets.newHashSet();

    //这里并不用并发插入
    Flowable.fromIterable(ids)
      .flatMapMaybe(this::search, false, 10)
      .doOnNext(wrapper -> successIds.add(wrapper.id))
      .buffer(100)
      .doOnNext(this::batchSaveToDB)
      .ignoreElements()
      .blockingAwait();
  }

  private Maybe<VideoWrapper> search(int id) {
    return searchCache(id)
      .switchIfEmpty(Maybe.defer(() -> httpSearch(id)))
      .map(videoInfo -> {
        VideoWrapper wrapper = new VideoWrapper();
        wrapper.id = id;
        wrapper.videoInfo = videoInfo;
        return wrapper;
      });
  }

  private Maybe<VideoInfo> searchCache(int id) {
    //TODO
    return null;
    //return RxUtils.toSingle(() -> rawSearchCache(id));
  }

  private Maybe<VideoInfo> httpSearch(int id) {
    Single<HttpResponse> s1 = httpSearch1(id)
      .timeout(10, TimeUnit.SECONDS)
      .retry(1);


    Single<HttpResponse> s2 = httpSearch2(id)
      .timeout(10, TimeUnit.SECONDS)
      .retry(1);

    //使用zip, 任意一方失败则总体失败
    return Single.zip(s1, s2, this::merge)
      .filter(x -> x.exists)
      .doOnError(error -> {
        //某个视频查询失败并不能算是总体的失败, 所以异常不能往外抛, 可以在这里处理异常
      })
      //当发生异常时忽略掉这个异常, 因为我们已经在上面处理了
      .onErrorComplete();
  }

  private Single<HttpResponse> httpSearch1(int id) {
    return RxUtils.toSingle(() -> rawHTTPSearch1(id));
  }

  private Single<HttpResponse> httpSearch2(int id) {
    return RxUtils.toSingle(() -> rawHTTPSearch2(id));
  }

}
