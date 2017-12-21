package com.xzchaoo.learn.jdk8.concurrent;

import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/3/21.
 */
public class ConcurrentApp {
	public static void sleep(ExecutorService es, long mills) {
		es.execute(() -> sleep(mills));
	}

	public static void sleep(long mills) {
		try {
			Thread.sleep(mills);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
	}

	@Test
	public void test_ThreadPoolExecutor_2() throws InterruptedException {
		ExecutorService es = Executors.newFixedThreadPool(4);
		for (int i = 0; i < 4; ++i) {
			es.execute(() -> {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					System.out.println("打断1");
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						System.out.println("打断2");
					}
				}
			});
		}
		sleep(20);
		// shutdownNow 会给线程一个中断 但不会阻止进一步的阻塞
		List<Runnable> list = es.shutdownNow();
		assertTrue(es.isShutdown());
		assertEquals(0, list.size());
		//assertFalse(es.isTerminated());
		sleep(300);
		assertTrue(es.isTerminated());
	}

	@Test
	public void test_ThreadPoolExecutor_3() throws InterruptedException {
		ExecutorService es = Executors.newFixedThreadPool(4);
		for (int i = 0; i < 4; ++i) {
			es.execute(() -> sleep(100));
		}
		sleep(20);
		// shutdownNow 会给线程一个中断
		es.shutdown();
		assertTrue(es.isShutdown());
		assertFalse(es.isTerminated());
		sleep(100);
		assertTrue(es.isTerminated());
	}

	@Test
	public void test_ThreadPoolExecutor() throws InterruptedException {
		//完全版
		ThreadPoolExecutor tpe = new ThreadPoolExecutor(
			2,
			2,
			10, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(),
			Thread::new,
			new ThreadPoolExecutor.AbortPolicy()
		);

		assertEquals(0, tpe.getPoolSize());

		//随便发个任务
		sleep(tpe, 100);
		Thread.sleep(10);
		//有了1个线程
		assertEquals(1, tpe.getPoolSize());

		sleep(tpe, 100);
		tpe.shutdown();

		assertTrue(tpe.isShutdown());
		assertTrue(tpe.isTerminating());
		assertFalse(tpe.isTerminated());

		//assertEquals(1, tpe.getActiveCount());
//		System.out.println(tpe.getActiveCount());
//		System.out.println(tpe.getPoolSize());
//		System.out.println(tpe.getLargestPoolSize());
//		System.out.println(tpe.getMaximumPoolSize());

		//System.out.println(tpe);

		//会等待所有在队列里的任务完成
		sleep(120);
		assertTrue(tpe.isShutdown());
		assertFalse(tpe.isTerminating());
		assertTrue(tpe.isTerminated());

		//已经关闭再添加任务会失败
		try {
			sleep(tpe, 100);
			assertTrue(false);
		} catch (RejectedExecutionException e) {
		}

		tpe.awaitTermination(1, TimeUnit.MINUTES);
		assertFalse(tpe.isTerminating());
		assertTrue(tpe.isTerminated());
	}

	@Test
	public void test_semaphore() throws InterruptedException {
		Semaphore s = new Semaphore(2);
		assertTrue(s.tryAcquire(1));
		assertTrue(s.tryAcquire(1));
		assertFalse(s.tryAcquire(1));
		s.release();
		s.release();
		assertTrue(s.tryAcquire(2));
		s.release(2);
		assertFalse(s.tryAcquire(3));
	}

	@Test
	public void test_CountDownLatch() throws InterruptedException {
		CountDownLatch c = new CountDownLatch(1);
		assertFalse(c.await(100, TimeUnit.MILLISECONDS));
		new Thread(() -> {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			c.countDown();
		}).start();
		assertFalse(c.await(200, TimeUnit.MILLISECONDS));
		assertTrue(c.await(200, TimeUnit.MILLISECONDS));
	}

	@Test
	public void test_CountDownLatch_2() throws InterruptedException {
		CountDownLatch c = new CountDownLatch(1);
		ExecutorService es = Executors.newFixedThreadPool(4);
		AtomicInteger ai = new AtomicInteger(0);
		for (int i = 0; i < 4; ++i) {
			es.execute(() -> {
				try {
					c.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ai.incrementAndGet();
			});
		}
		Thread.sleep(100);
		assertEquals(0, ai.get());
		c.countDown();
		Thread.sleep(100);
		assertEquals(4, ai.get());
	}

	@Test
	public void test_sync() {
		//lock sync
		assertTrue(true);
	}

	@Test
	public void test_ReentrantLock() {
		ReentrantLock rl = new ReentrantLock();
		assertTrue(rl.tryLock());
		assertTrue(rl.isLocked());

		assertTrue(rl.tryLock());
		assertTrue(rl.isLocked());

		rl.unlock();
		assertTrue(rl.isLocked());

		rl.unlock();
		assertFalse(rl.isLocked());

		assertTrue(rl.tryLock());
		assertTrue(rl.isLocked());

		new Thread(() -> {
			assertTrue(rl.isLocked());
			assertFalse(rl.tryLock());
		}).start();
		sleep(100);
	}

	@Test
	public void test_ReadWriteLock() {
		ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
		assertFalse(lock.isWriteLocked());
		assertTrue(lock.readLock().tryLock());

		assertFalse(lock.isWriteLocked());
		assertTrue(lock.readLock().tryLock());

		lock.readLock().unlock();
		assertFalse(lock.isWriteLocked());

		assertFalse(lock.writeLock().tryLock());

		lock.readLock().unlock();
		assertFalse(lock.isWriteLocked());
		assertFalse(lock.isWriteLockedByCurrentThread());

		assertTrue(lock.writeLock().tryLock());
		assertTrue(lock.isWriteLocked());
		assertTrue(lock.isWriteLockedByCurrentThread());

	}

	public static class User {
		public int id;
		public String name;

		public User(int id, String name) {
			this.id = id;
			this.name = name;
		}

		@Override
		public String toString() {
			return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				'}';
		}
	}

	@Test
	public void test_PriorityQueue() {
		List<User> list = IntStream.range(0, 100).mapToObj(id -> new User(id, "x" + id)).collect(Collectors.toList());
		Collections.shuffle(list);
		PriorityQueue<User> pq = new PriorityQueue<>(Comparator.comparingInt(o -> o.id));
		pq.addAll(list);
		for (int i = 0; i < 100; ++i) {
			assertEquals(i, pq.poll().id);
		}
	}

	@Test
	public void test_CyclicBarrier() throws BrokenBarrierException, InterruptedException {
		CyclicBarrier cb = new CyclicBarrier(2);
		ExecutorService es = Executors.newFixedThreadPool(2);
		for (int i = 0; i < 2; ++i) {
			es.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println("阶段1 " + Thread.currentThread().getId());
					sleep(100);
					try {
						//所有的子线程执行cb 然后等待
						cb.await();
					} catch (Exception e) {
					}
					System.out.println("阶段2 " + Thread.currentThread().getId());
					sleep(110);
				}
			});
		}
		es.shutdown();
		es.awaitTermination(1, TimeUnit.DAYS);
	}

	@Test
	public void test_Phaser() throws InterruptedException {
		Phaser p = new Phaser();
		ExecutorService es = Executors.newFixedThreadPool(4);
		for (int i = 0; i < 4; ++i) {
			es.execute(new Runnable() {
				@Override
				public void run() {
					Random r = ThreadLocalRandom.current();

					p.register();

					sleep(100);
					System.out.println("阶段1 " + Thread.currentThread().getId());
					if (r.nextBoolean()) {
						int phase = p.arrive();
						p.awaitAdvance(phase);
					} else {
						p.arriveAndDeregister();
						return;
					}

					sleep(100);
					System.out.println("阶段2 " + Thread.currentThread().getId());
					p.arriveAndAwaitAdvance();

					sleep(100);
					System.out.println("阶段3 " + Thread.currentThread().getId());
					p.arriveAndAwaitAdvance();
				}
			});
		}
		es.shutdown();
		es.awaitTermination(1, TimeUnit.DAYS);
	}
}
