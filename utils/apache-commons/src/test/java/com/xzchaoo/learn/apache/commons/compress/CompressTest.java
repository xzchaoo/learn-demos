package com.xzchaoo.learn.apache.commons.compress;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CompressTest {
	@Test
	public void test1() throws CompressorException, IOException {
		byte[] data = ("package com.xzchaoo.learn.apache.commons.compress;\n" +
			"\n" +
			"import org.apache.commons.compress.compressors.CompressorException;\n" +
			"import org.apache.commons.compress.compressors.CompressorOutputStream;\n" +
			"import org.apache.commons.compress.compressors.CompressorStreamFactory;\n" +
			"import org.junit.Test;\n" +
			"\n" +
			"import java.io.ByteArrayOutputStream;\n" +
			"import java.io.IOException;\n" +
			"import java.nio.charset.StandardCharsets;\n" +
			"\n" +
			"public class CompressTest {\n" +
			"\t@Test\n" +
			"\tpublic void test1() throws CompressorException, IOException {\n" +
			"\t\tfor (String type : CompressorStreamFactory.getSingleton().getOutputStreamCompressorNames()) {\n" +
			"\t\t\ttry {\n" +
			"\t\t\t\tSystem.out.println(\"type=\" + type);\n" +
			"\t\t\t\tByteArrayOutputStream baos = new ByteArrayOutputStream();\n" +
			"\t\t\t\tCompressorOutputStream cos = CompressorStreamFactory.getSingleton().createCompressorOutputStream(type, baos);\n" +
			"\t\t\t\tcos.write(\"中文\".getBytes(StandardCharsets.UTF_8));\n" +
			"\t\t\t\tcos.close();\n" +
			"\t\t\t\tSystem.out.println(\"size = \" + baos.size());\n" +
			"\t\t\t} catch (NoClassDefFoundError e) {\n" +
			"\n" +
			"\t\t\t}\n" +
			"\t\t}\n" +
			"\t}\n" +
			"}\n").getBytes(StandardCharsets.UTF_8);
		final int count = 1000;
		for (String type : CompressorStreamFactory.getSingleton().getOutputStreamCompressorNames()) {
			try {
				long sum = 0;
				int size = 0;
				for (int i = 0; i < count; ++i) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					CompressorOutputStream cos = CompressorStreamFactory.getSingleton().createCompressorOutputStream(type, baos);
					long begin = System.currentTimeMillis();
					cos.write(data);
					cos.close();
					long end = System.currentTimeMillis();
					sum += (end - begin);
					size = baos.size();
				}
				System.out.println("type=" + type + " oldSize=" + data.length + " newSize = " + size + " d=" + sum + " / " + count);
			} catch (NoClassDefFoundError e) {

			}
		}
	}
}
