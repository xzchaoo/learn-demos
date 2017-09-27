package com.xzchaoo.learn.nanohttpd.utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/4/25.
 */
public class MyIOUtils {
	public static final int EOF = -1;

	public static byte[] readFully(final InputStream input, final int length) throws IOException {
		final byte[] buffer = new byte[length];
		readFully(input, buffer, 0, buffer.length);
		return buffer;
	}

	public static void readFully(final InputStream input, final byte[] buffer, final int offset, final int length)
		throws IOException {
		final int actual = read(input, buffer, offset, length);
		if (actual != length) {
			throw new EOFException("Length to read: " + length + " actual: " + actual);
		}
	}

	public static int read(final InputStream input, final byte[] buffer, final int offset, final int length)
		throws IOException {
		if (length < 0) {
			throw new IllegalArgumentException("Length must not be negative: " + length);
		}
		int remaining = length;
		while (remaining > 0) {
			final int location = length - remaining;
			final int count = input.read(buffer, offset + location, remaining);
			if (EOF == count) { // EOF
				break;
			}
			remaining -= count;
		}
		return length - remaining;
	}
}
