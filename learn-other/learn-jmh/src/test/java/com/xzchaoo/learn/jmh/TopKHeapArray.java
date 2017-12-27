package com.xzchaoo.learn.jmh;

import java.util.Comparator;

/**
 * @author xzchaoo
 * @date 2017/12/28
 */
public class TopKHeapArray<T> {
	private final T[] buffer;
	private final int k;
	private final Comparator<T> comparator;
	private int index;

	public TopKHeapArray(int k, Comparator<T> comparator) {
		this.k = k;
		this.buffer = (T[]) new Object[k];
		this.comparator = comparator;
	}

	public void add(T t) {
		if (index == k) {
			if (comparator.compare(t, buffer[0]) < 0) {
				buffer[0] = t;
				siftDown(0);
			}
			buffer[0] = t;
		} else {
			buffer[index++] = t;
			if (index == k) {
				buildHeap();
			}
		}
	}

	private void buildHeap() {
		for (int i = k / 2; i >= 0; --i) {
			siftDown(i);
		}
	}

	private void siftDown(int x) {
		int t = x << 1;
		while (t < k) {
			if (t + 1 < k && comparator.compare(buffer[t + 1], buffer[t]) > 0) {
				++t;
			}
			if (comparator.compare(buffer[t], buffer[x]) > 0) {
				T temp = buffer[x];
				buffer[x] = buffer[t];
				buffer[t] = temp;
				x = t;
				t = t << 1;
			} else {
				break;
			}
		}
	}

	public T[] getBuffer() {
		return buffer;
	}

	public int getAvailableLength() {
		return index;
	}
}
