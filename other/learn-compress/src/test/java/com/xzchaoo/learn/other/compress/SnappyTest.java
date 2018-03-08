package com.xzchaoo.learn.other.compress;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.xerial.snappy.BitShuffle;
import org.xerial.snappy.Snappy;
import org.xerial.snappy.SnappyFramedInputStream;
import org.xerial.snappy.SnappyFramedOutputStream;
import org.xerial.snappy.SnappyInputStream;
import org.xerial.snappy.SnappyOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * snappy依赖了一些外部的DLL和SO文件, 这些文件被打在jar包里, 运行时解压到临时目录 然后利用 System.load 进行加载
 * http://ning.github.io/jvm-compressor-benchmark/results/canterbury-roundtrip-2011-07-28/index.html
 */
public class SnappyTest {
	@Test
	public void test1() throws Exception {
		//可以将数组先压缩成byte[]
		int[] data = new int[]{1, 3, 34, 43, 34};
		byte[] shuffledByteArray = BitShuffle.shuffle(data);
		//然后再进行snappy压缩
		byte[] compressed = Snappy.compress(shuffledByteArray);
		byte[] uncompressed = Snappy.uncompress(compressed);
		int[] result = BitShuffle.unshuffleIntArray(uncompressed);
		System.out.println(Arrays.asList(result));
	}

	/**
	 * 注意格式 不同的压缩方式和解压方式不一定兼容, 具体看官方文档
	 *
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		//直接操作byte[]
		byte[] compressedData = Snappy.compress("admin".getBytes(StandardCharsets.UTF_8));
		byte[] originalData = Snappy.uncompress(compressedData);
		assertEquals("admin", new String(originalData, StandardCharsets.UTF_8));

		//基于流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SnappyOutputStream sos = new SnappyOutputStream(baos);
		sos.write("admin".getBytes(StandardCharsets.UTF_8));
		sos.flush();
		sos.close();
		SnappyInputStream sis = new SnappyInputStream(new ByteArrayInputStream(baos.toByteArray()));
		assertEquals("admin", new String(IOUtils.readFully(sis, sis.available()), StandardCharsets.UTF_8));

		baos = new ByteArrayOutputStream();
		SnappyFramedOutputStream sfos = new SnappyFramedOutputStream(baos);
		sfos.write("admin".getBytes(StandardCharsets.UTF_8));
		sfos.flush();
		sfos.close();
		SnappyFramedInputStream sfis = new SnappyFramedInputStream(new ByteArrayInputStream(baos.toByteArray()));
		//sfis的available似乎返回0
		baos = new ByteArrayOutputStream();
		IOUtils.copy(sfis, baos);
		assertEquals("admin", new String(baos.toByteArray(), StandardCharsets.UTF_8));
	}
}
