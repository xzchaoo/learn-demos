package com.xzchaoo.learn.jmh.example;

import org.apache.commons.io.IOUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * TODO 这个测试没有构建好
 * created by zcxu at 2017/12/1
 *
 * @author zcxu
 */
@Threads(1)
@Fork(1)
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3, time = 100, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 400, timeUnit = TimeUnit.MILLISECONDS)
public class StreamWrapperTest {
	@State(Scope.Benchmark)
	public static class StreamData {
		@Param({"1024", "10240", "51200"})
		public int size;
		public byte[] data;

		@Setup
		public void setup() throws Exception {
			byte[] data = new byte[size];
			new Random().nextBytes(data);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gos = new GZIPOutputStream(baos);
			gos.write(data);
			gos.finish();
			gos.flush();
			gos.close();
			this.data = baos.toByteArray();
		}
	}

	@Benchmark
	public void test1(StreamData data, Blackhole b) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(data.data);
		GZIPInputStream gis = new GZIPInputStream(bais);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(gis, baos);
		byte[] decompressedData = baos.toByteArray();
		baos = new ByteArrayOutputStream();
		GZIPOutputStream gos = new GZIPOutputStream(baos);
		gos.write(decompressedData);
		gos.finish();
		gos.flush();
		gos.close();
		String s = Base64.getEncoder().encodeToString(baos.toByteArray());
		b.consume(s);
	}

	@Benchmark
	public void test2(StreamData data, Blackhole b) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(data.data);
		GZIPInputStream gis = new GZIPInputStream(bais);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStream os = Base64.getEncoder().wrap(baos);
		GZIPOutputStream gos = new GZIPOutputStream(os);
		IOUtils.copy(gis, gos);
		gos.finish();
		gos.flush();
		gos.close();
		os.flush();
		os.close();
		String s = new String(baos.toByteArray());
		b.consume(s);
	}
}
