package com.xzchaoo.learn.alg.twoditopn;

import org.eclipse.collections.impl.set.mutable.primitive.IntHashSet;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * 给定两个非递减的数组A和B, 设a是A中的元素,b是B中的元素, 求最小的k个a+b的值
 * created by xzchaoo at 2017/11/24
 *
 * @author xzchaoo
 */
public class TwoDimensionTopN {
	private int[] alg1(int[] a, int[] b, int n) {
		int[] c = new int[a.length * b.length];
		for (int i = 0; i < a.length; ++i) {
			for (int j = 0; j < b.length; ++j) {
				c[i * b.length + j] = a[i] + b[j];
			}
		}
		Arrays.sort(c);
		int[] r = new int[n];
		System.arraycopy(c, 0, r, 0, n);
		return r;
	}

	private int[] generateIntArray(Random r, int n, int bound) {
		int[] a = new int[n];
		for (int i = 0; i < n; ++i) {
			a[i] = 1 + r.nextInt(bound);
		}
		Arrays.sort(a);
		return a;
	}

	@Test
	public void test() {
		Random r = new Random(7086);
		int[] a = generateIntArray(r, 500, 10000);
		int[] b = generateIntArray(r, 500, 10000);
		int n = 1000;
		for (int i = 0; i < 100; ++i) {
			long begin1 = System.currentTimeMillis();
			int[] r1 = alg1(a, b, n);
			long end1 = System.currentTimeMillis();

			long begin2 = System.currentTimeMillis();
			int[] r2 = alg2(a, b, n);
			long end2 = System.currentTimeMillis();

			long begin3 = System.currentTimeMillis();
			int[] r3 = alg3(a, b, n);
			long end3 = System.currentTimeMillis();

			System.out.println((end1 - begin1) + " " + (end2 - begin2) + " " + (end3 - begin3));
			//if (!Arrays.equals(r1, r2) || !Arrays.equals(r1, r3)) {
			if (!Arrays.equals(r1, r2) || !Arrays.equals(r1, r3)) {
				System.out.println(Arrays.toString(r1));
				System.out.println(Arrays.toString(r2));
				System.out.println(Arrays.toString(r3));
				System.out.println("fail");
				break;
			}
		}
	}

	static class E {
		final int x;
		final int y;
		final int value;

		E(int x, int y, int value) {
			this.x = x;
			this.y = y;
			this.value = value;
		}
	}

	private int[] alg3(int[] a, int[] b, int n) {
		PriorityQueue<E> pending = new PriorityQueue<>(n, Comparator.comparingInt(x -> x.value));
		IntHashSet map = new IntHashSet(n * 2);
		map.add(0);
		pending.add(new E(0, 0, a[0] + b[0]));
		int[] r = new int[n];
		int findCount = 0;
		while (!pending.isEmpty()) {
			E e = pending.poll();
			if (e.x + 1 < a.length) {
				int xx = e.x + 1;
				if (map.add(xx * b.length + e.y)) {
					pending.add(new E(xx, e.y, a[xx] + b[e.y]));
				}
//				if (!map[xx][e.y]) {
//					map[xx][e.y] = true;
//					pending.add(new E(xx, e.y, a[xx] + b[e.y]));
//				}
			}
			if (e.y + 1 < b.length) {
				int yy = e.y + 1;
				if (map.add(e.x * b.length + yy)) {
					pending.add(new E(e.x, yy, a[e.x] + b[yy]));
				}
//				if (!map[e.x][yy]) {
//					map[e.x][yy] = true;
//					pending.add(new E(e.x, yy, a[e.x] + b[yy]));
//				}
			}
			r[findCount++] = e.value;
			if (findCount == n) {
				break;
			}
		}
		return r;
	}

	private int[] alg2(int[] a, int[] b, int n) {
		LinkedList<int[]> oldPending = null;
		LinkedList<int[]> pending = new LinkedList<>();
		LinkedList<int[]> eq = new LinkedList<>();
		//boolean[][] map = new boolean[a.length][b.length];
		//map[0][0] = true;
		IntHashSet map = new IntHashSet(n);
		//Set<Integer> map = new HashSet<>();
		map.add(0);
		pending.add(new int[]{0, 0});
		int[] r = new int[n];
		int findCount = 0;
		while (!pending.isEmpty() || !eq.isEmpty()) {
			int[] fpos;
			int fvalue;
			if (eq.size() > 0) {
				fpos = eq.removeFirst();
				fvalue = a[fpos[0]] + b[fpos[1]];
			} else {
				oldPending = pending;
				pending = new LinkedList<>();
				fpos = oldPending.getFirst();
				fvalue = a[fpos[0]] + b[fpos[1]];
				for (int[] pos : oldPending) {
					if (fpos == pos) {
						continue;
					}
					int value = a[pos[0]] + b[pos[1]];
					int cmp = Integer.compare(value, fvalue);
					if (cmp < 0) {
						if (eq.size() > 0) {
							pending.addAll(eq);
							eq.clear();
						}
						pending.add(fpos);
						fpos = pos;
						fvalue = value;
					} else if (cmp == 0) {
						eq.add(pos);
					} else {
						pending.add(pos);
					}
				}
			}
			if (fpos[0] + 1 < a.length) {
				if (map.add((fpos[0] + 1) * b.length + fpos[1])) {
					pending.add(new int[]{fpos[0] + 1, fpos[1]});
				}
//				if (!map[fpos[0] + 1][fpos[1]]) {
//					map[fpos[0] + 1][fpos[1]] = true;
//					pending.add(new int[]{fpos[0] + 1, fpos[1]});
//				}
			}
			if (fpos[1] + 1 < b.length) {
				if (map.add((fpos[0]) * b.length + fpos[1] + 1)) {
					pending.add(new int[]{fpos[0], fpos[1] + 1});
				}
//				if (!map[fpos[0]][fpos[1] + 1]) {
//					map[fpos[0]][fpos[1] + 1] = true;
//					pending.add(new int[]{fpos[0], fpos[1] + 1});
//				}
			}
			r[findCount++] = fvalue;
			if (findCount == n) {
				break;
			}
		}
		return r;
	}
}
