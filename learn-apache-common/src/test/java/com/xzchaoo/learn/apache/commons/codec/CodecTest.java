package com.xzchaoo.learn.apache.commons.codec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class CodecTest {
	@Test
	public void test_base64() {
		String b64 = Base64.encodeBase64String("binary data".getBytes(StandardCharsets.UTF_8));
		String s = new String(Base64.decodeBase64(b64), StandardCharsets.UTF_8);
		assertEquals("binary data", s);

		Base64InputStream is = null;
		Base64OutputStream os = null;
	}

	@Test
	public void test_digest() {
		assertEquals("21232f297a57a5a743894a0e4a801fc3", DigestUtils.md5Hex("admin"));
		assertEquals("d033e22ae348aeb5660fc2140aec35850c4da997", DigestUtils.sha1Hex("admin"));
		assertEquals("8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918", DigestUtils.sha256Hex("admin"));
		assertEquals("9ca694a90285c034432c9550421b7b9dbd5c0f4b6673f05f6dbce58052ba20e4248041956ee8c9a2ec9f10290cdc0782", DigestUtils.sha384Hex("admin"));
		assertEquals("c7ad44cbad762a5da0a452f9e854fdc1e0e7a52a38015f23f3eab1d80b931dd472634dfac71cd34ebc35d16ab7fb8a90c81f975113d6c7538dc69dd8de9077ec", DigestUtils.sha512Hex("admin"));
	}
}
